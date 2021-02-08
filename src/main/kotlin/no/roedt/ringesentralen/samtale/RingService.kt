package no.roedt.ringesentralen.samtale

import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import no.roedt.ringesentralen.samtale.resultat.AutentisertResultatFraSamtaleRequest
import no.roedt.ringesentralen.samtale.resultat.KoronaspesifikkeResultat
import no.roedt.ringesentralen.samtale.resultat.Resultat
import no.roedt.ringesentralen.samtale.resultat.ResultatFraSamtaleRequest
import java.sql.Timestamp
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

interface RingService {
    fun hentNestePersonAaRinge(userId: UserId): NestePersonAaRingeResponse?
    fun startSamtale(request: AutentisertStartSamtaleRequest)
    fun registrerResultatFraSamtale(autentisertRequest: AutentisertResultatFraSamtaleRequest)
    fun noenRingerTilbake(request: AutentisertRingerTilbakeRequest): Person
}

@ApplicationScoped
class RingServiceBean(
    val personRepository: PersonRepository,
    val entityManager: EntityManager,
    val databaseUpdater: DatabaseUpdater,
): RingService {

    override fun hentNestePersonAaRinge(userId: UserId): NestePersonAaRingeResponse? =
        entityManager
            .createNativeQuery("SELECT v.id FROM v_personerSomKanRinges v " +
                    "WHERE lokallag = '${getLokallag(userId)}'")
            .resultList
            .firstOrNull()
            ?.let { it as Int }
            ?.let { personRepository.findById(it.toLong()) }
            ?.let { NestePersonAaRingeResponse(person = it, tidlegareSamtalar = getTidlegareSamtalarMedDennePersonen(
                it.telefonnummer ?: "-1"))}
            ?.also {
                databaseUpdater.update("call sp_lagreOppslag(${it.person.id}, ${userId.userId})")
            }

    fun getLokallag(userId: UserId) =
        personRepository.find("hypersysID", userId.userId).firstResult<Person>().lokallag

    private fun getTidlegareSamtalarMedDennePersonen(oppringtNummer: String): List<Samtale> =
        entityManager.createNativeQuery("SELECT resultat, ringerNavn, datetime, kommentar, ringtNavn FROM `v_samtalerResultat` WHERE oppringtNummer = '$oppringtNummer'")
            .resultList
            .map { it as Array<*> }
            .map { Samtale(
                resultat = it[0] as String,
                ringer = it[1] as String,
                tidspunkt = (it[2] as Timestamp).toString(),
                kommentar = it[3] as String,
                ringtNummer = oppringtNummer,
                ringtNavn = it[4] as String
            ) }
            .toList()

    override fun startSamtale(request: AutentisertStartSamtaleRequest) {
        val ringerId = hypersysIDTilRingerId(request.userId)
        databaseUpdater.update("CALL sp_startSamtale(${request.skalRingesID()}, $ringerId)")
    }

    override fun registrerResultatFraSamtale(autentisertRequest: AutentisertResultatFraSamtaleRequest) {
        val request = autentisertRequest.request
        assert(request.isGyldigResultat())
        databaseUpdater.update("CALL sp_registrerSamtale(${request.ringtID}, ${hypersysIDTilRingerId(autentisertRequest.userId)}, ${request.resultat.nr}, '${request.kommentar}')")

        lagreResultat(getNesteGroupID(request), request)
    }

    private fun getNesteGroupID(request: ResultatFraSamtaleRequest): GroupID? {
        return when {
            request.resultat.nesteGroupID != null -> request.resultat.nesteGroupID
            erFleireEnnToIkkeSvar(request) -> GroupID.Ferdigringt
            else -> null
        }
    }

    private fun lagreResultat(nesteGroupID: GroupID?, request: ResultatFraSamtaleRequest) {
        nesteGroupID?.nr?.let { databaseUpdater.update("CALL sp_updateGroupID(${request.ringtID}, $it)") }
        if (request.skalRegistrere()) {
            registrerKoronaspesifikkeResultat(request)
        }
    }

    override fun noenRingerTilbake(request: AutentisertRingerTilbakeRequest): Person {
        val ringer = hypersysIDTilRingerId(request.userId)
        val oppringtNummer = request.ringtNummer()
        val personSomRingerTilbake: Person = personRepository.find("telefonnummer", oppringtNummer).firstResult()
        if (entityManager.createNativeQuery("SELECT 1 FROM v_noenRingerTilbake WHERE telefonnummer = '$oppringtNummer' AND ringer = '$ringer' LIMIT 1").resultList.isEmpty()) {
            throw Exception("Du kan berre registrere å bli ringt opp frå folk du har ringt tidlegare.")
        }
        startSamtale(
            AutentisertStartSamtaleRequest(
                userId = request.userId,
                StartSamtaleRequest(
                skalRingesID = personSomRingerTilbake.id
        )))
        return personSomRingerTilbake
    }

    private fun erFleireEnnToIkkeSvar(request: ResultatFraSamtaleRequest): Boolean {
        val resultat: List<Int>? = databaseUpdater.updateWithResult("select resultat from `samtale` where ringt = ${request.ringtID} and resultat = 0")?.map { it as Int }
        val fleireEnnToIkkeSvar: Boolean = (resultat?.filter { it == 0 }?.count() ?: 0) > 2
        val ingenSvar: Boolean = (resultat?.filter { it != 0 && it != 9 }?.count() ?: 0) == 0
        return ingenSvar && fleireEnnToIkkeSvar && request.resultat == Resultat.Ikke_svar
    }

    private fun registrerKoronaspesifikkeResultat(request: ResultatFraSamtaleRequest) {
        val resultat = request.modusspesifikkeResultat as KoronaspesifikkeResultat
        databaseUpdater.update("CALL sp_registrerOppfoelgingKorona(${request.ringtID}, ${resultat.vilHaKoronaprogram}, ${resultat.vilBliMerAktiv}, ${resultat.vilHaValgkampsbrev}, ${request.vilIkkeBliRingt})")
    }

    fun hypersysIDTilRingerId(userId: UserId) =
        entityManager.createNativeQuery(
            "select ringer.id from ringer inner join person on person.id = ringer.personId and person.hypersysID = ${userId.userId} "
        ).resultList.first()
}
