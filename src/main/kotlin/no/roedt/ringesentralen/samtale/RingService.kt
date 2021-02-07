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

    private fun getTidlegareSamtalarMedDennePersonen(calledPhone: String): List<Samtale> =
        entityManager.createNativeQuery("SELECT result, ringerNavn, datetime, comment FROM `v_callsResult` WHERE calledPhone = '$calledPhone'")
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
        val calledPhone = personRepository.findById(request.skalRingesID()).phone
        databaseUpdater.update("CALL sp_startSamtale($calledPhone, $ringerId)")
    }

    override fun registrerResultatFraSamtale(autentisertRequest: AutentisertResultatFraSamtaleRequest) {
        val request = autentisertRequest.resultatFraSamtaleRequest
        assert(request.result in request.modus.gyldigeResultattyper)
        val calledPhone = personRepository.findById(request.ringtID).phone
        val ringerId = hypersysIDTilRingerId(autentisertRequest.userId)
        databaseUpdater.update("CALL sp_registrerSamtale($calledPhone, $ringerId, ${request.result.nr}, '${request.kommentar}')")
        val nesteGroupID: GroupID? = when  {
            request.result.nesteGroupID != null -> request.result.nesteGroupID
            erFleireEnnToIkkeSvar(calledPhone, request) -> GroupID.Ferdigringt
            else -> null
        }
        nesteGroupID?.nr?.let { databaseUpdater.updateWithResult("CALL sp_updateGroupID($calledPhone, $it)") }
        if (request.modus == Modus.Korona && request.result == Resultat.Svarte) {
            registrerKoronaspesifikkeResultat(request, calledPhone)
        }
    }

    override fun noenRingerTilbake(request: AutentisertRingerTilbakeRequest): RingbarPerson {
        val ringer = hypersysIDTilRingerId(request.userId)
        val calledPhone = request.ringtNummer()
        val personSomRingerTilbake: RingbarPerson = personRepository.find("phone", calledPhone).firstResult()
        if (entityManager.executeQuery("SELECT 1 FROM v_noenRingerTilbake WHERE phone = '$calledPhone' AND ringer = '$ringer' LIMIT 1").isEmpty()) {
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

    private fun erFleireEnnToIkkeSvar(calledPhone: String, request: ResultatFraSamtaleRequest): Boolean {
        val resultat: List<Int>? = databaseUpdater.updateWithResult("select result from `call` where calledPhone = $calledPhone and result = 0")?.map { it as Int }
        val fleireEnnToIkkeSvar: Boolean = (resultat?.filter { it == 0 }?.count() ?: 0) > 2
        val ingenSvar: Boolean = (resultat?.filter { it != 0 && it != 9 }?.count() ?: 0) == 0
        return ingenSvar && fleireEnnToIkkeSvar && request.result == Resultat.Ikke_svar
    }

    private fun registrerKoronaspesifikkeResultat(request: ResultatFraSamtaleRequest, calledPhone: String) {
        val resultat: KoronaspesifikkeResultat = request.modusspesifikkeResultat as KoronaspesifikkeResultat
        databaseUpdater.update("CALL sp_registrerOppfoelgingKorona($calledPhone, ${resultat.vilHaKoronaprogram}, ${resultat.vilBliMerAktiv}, ${resultat.vilHaValgkampsbrev}, ${request.vilIkkeBliRingt})")
    }

    fun EntityManager.executeQuery(query: String) = entityManager.createNativeQuery(query).resultList

    private fun hypersysIDTilRingerId(userId: UserId) =
        entityManager.createNativeQuery(
            "select ringer.id from ringer inner join person on person.id = ringer.personId and person.hypersysID = ${userId.userId} "
        ).resultList.first()
}
