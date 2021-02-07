package no.roedt.ringesentralen.samtale

import UserId
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.PersonRepository
import java.sql.Timestamp
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

interface RingService {
    fun hentNestePersonAaRinge(nestePersonAaRingeRequest: AutentisertNestePersonAaRingeRequest): NestePersonAaRingeResponse?
    fun startSamtale(request: AutentisertStartSamtaleRequest)
    fun registrerResultatFraSamtale(request: AutentisertResultatFraSamtaleRequest)
    fun noenRingerTilbake(request: AutentisertRingerTilbakeRequest): RingbarPerson
}

@ApplicationScoped
class RingServiceBean(
        val personRepository: PersonRepository,
        val entityManager: EntityManager,
        val databaseUpdater: DatabaseUpdater
): RingService {

    //TODO: Vurder om dette skal loggast
    override fun hentNestePersonAaRinge(nestePersonAaRingeRequest: AutentisertNestePersonAaRingeRequest): NestePersonAaRingeResponse? =
        entityManager
            .createNativeQuery("SELECT v.id FROM v_personerSomKanRinges v WHERE lokallag = '${nestePersonAaRingeRequest.lokallagId()}'")
            .resultList
            .firstOrNull()
            ?.let { it as Int }
            ?.let { personRepository.findById(it.toLong()) }
            ?.let { NestePersonAaRingeResponse(ringbarPerson = it, tidlegareSamtalar = getTidlegareSamtalarMedDennePersonen(it.phone))}

    private fun getTidlegareSamtalarMedDennePersonen(oppringtNummer: String): List<Samtale> =
        entityManager.createNativeQuery("SELECT resultat, ringerNavn, datetime, kommentar FROM `v_callsResultat` WHERE oppringtNummer = '$oppringtNummer'")
            .resultList
            .map { it as Array<*> }
            .map { Samtale(
                resultat = it[0] as String,
                ringer = it[1] as String,
                tidspunkt = (it[2] as Timestamp).toLocalDateTime(),
                kommentar = it[3] as String
            ) }
            .toList()

    override fun startSamtale(request: AutentisertStartSamtaleRequest) {
        val ringerId = hypersysIDTilRingerId(request.userId)
        val oppringtNummer = personRepository.findById(request.skalRingesID()).phone
        databaseUpdater.update("CALL sp_startSamtale($oppringtNummer, $ringerId)")
    }

    override fun registrerResultatFraSamtale(autentisertRequest: AutentisertResultatFraSamtaleRequest) {
        val request = autentisertRequest.resultatFraSamtaleRequest
        assert(request.resultat in request.modus.gyldigeResultattyper)
        val oppringtNummer = personRepository.findById(request.ringtID).phone
        val ringerId = hypersysIDTilRingerId(autentisertRequest.userId)
        databaseUpdater.update("CALL sp_registrerSamtale($oppringtNummer, $ringerId, ${request.resultat.nr}, '${request.kommentar}')")
        val nesteGroupID: GroupID? = when  {
            request.resultat.nesteGroupID != null -> request.resultat.nesteGroupID
            erFleireEnnToIkkeSvar(oppringtNummer, request) -> GroupID.Ferdigringt
            else -> null
        }
        nesteGroupID?.nr?.let { databaseUpdater.updateWithResult("CALL sp_updateGroupID($oppringtNummer, $it)") }
        if (request.modus == Modus.Korona && request.resultat == Resultat.Svarte) {
            registrerKoronaspesifikkeResultat(request, oppringtNummer)
        }
    }

    override fun noenRingerTilbake(request: AutentisertRingerTilbakeRequest): RingbarPerson {
        val ringer = hypersysIDTilRingerId(request.userId)
        val oppringtNummer = request.ringtNummer()
        val personSomRingerTilbake: RingbarPerson = personRepository.find("phone", oppringtNummer).firstResult()
        if (entityManager.executeQuery("SELECT 1 FROM v_noenRingerTilbake WHERE phone = '$oppringtNummer' AND ringer = '$ringer' LIMIT 1").isEmpty()) {
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

    private fun erFleireEnnToIkkeSvar(oppringtNummer: String, request: ResultatFraSamtaleRequest): Boolean {
        val resultat: List<Int>? = databaseUpdater.updateWithResult("select resultat from `call` where oppringtNummer = $oppringtNummer and resultat = 0")?.map { it as Int }
        val fleireEnnToIkkeSvar: Boolean = (resultat?.filter { it == 0 }?.count() ?: 0) > 2
        val ingenSvar: Boolean = (resultat?.filter { it != 0 && it != 9 }?.count() ?: 0) == 0
        return ingenSvar && fleireEnnToIkkeSvar && request.resultat == Resultat.Ikke_svar
    }

    private fun registrerKoronaspesifikkeResultat(request: ResultatFraSamtaleRequest, oppringtNummer: String) {
        val resultat: KoronaspesifikkeResultat = request.modusspesifikkeResultat as KoronaspesifikkeResultat
        databaseUpdater.update("CALL sp_registrerOppfoelgingKorona($oppringtNummer, ${resultat.vilHaKoronaprogram}, ${resultat.vilBliMerAktiv}, ${resultat.vilHaValgkampsbrev}, ${request.vilIkkeBliRingt})")
    }

    fun EntityManager.executeQuery(query: String) = entityManager.createNativeQuery(query).resultList

    private fun hypersysIDTilRingerId(userId: UserId) =
        entityManager.createNativeQuery(
            "select ringer.id from ringer inner join person on person.id = ringer.personId and person.hypersysID = ${userId.userId} "
        ).resultList.first()
}
