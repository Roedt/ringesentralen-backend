package no.roedt.ringesentralen.samtale

import UserId
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.PersonRepository
import java.time.LocalDateTime
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

interface RingService {
    fun hentNestePersonAaRinge(nestePersonAaRingeRequest: NestePersonAaRingeRequest): RingbarPerson?
    fun startSamtale(request: StartSamtaleRequest): StartSamtaleResponse
    fun registrerResultatFraSamtale(request: AutentisertResultatFraSamtaleRequest): ResultatFraSamtaleResponse
    fun noenRingerTilbake(request: RingerTilbakeRequest): RingbarPerson
}

@ApplicationScoped
class RingServiceBean(
        val personRepository: PersonRepository,
        val entityManager: EntityManager,
        val databaseUpdater: DatabaseUpdater
): RingService {

    //TODO: Vurder om dette skal loggast
    override fun hentNestePersonAaRinge(nestePersonAaRingeRequest: NestePersonAaRingeRequest): RingbarPerson? =
            entityManager
                    .createNativeQuery("SELECT v.id FROM v_personerSomKanRinges v WHERE lokallag = '${nestePersonAaRingeRequest.lokallag.id}'")
                    .resultList
                    .firstOrNull()
                    ?.let { it as Int}
                    ?.let { personRepository.findById(it.toLong()) }

    override fun startSamtale(request: StartSamtaleRequest): StartSamtaleResponse {
        val callerPhone = personRepository.findById(request.ringerID).phone
        val calledPhone = personRepository.findById(request.skalRingesID).phone
        databaseUpdater.update("CALL sp_startSamtale($calledPhone, $callerPhone)")
        return StartSamtaleResponse(request.ringerID, request.skalRingesID, LocalDateTime.now())
    }

    override fun registrerResultatFraSamtale(autentisertRequest: AutentisertResultatFraSamtaleRequest): ResultatFraSamtaleResponse {
        val request = autentisertRequest.resultatFraSamtaleRequest
        val callerPhone = personRepository.findById(hypersysIdToPersonId(autentisertRequest.userId)).phone
        val calledPhone = personRepository.findById(request.ringtID).phone
        assert(request.result in request.modus.gyldigeResultattyper)
        databaseUpdater.update("CALL sp_registrerSamtale($calledPhone, $callerPhone, ${request.result.nr}, '${request.kommentar}')")
        val nesteGroupID: GroupID? = when  {
            request.result.nesteGroupID != null -> request.result.nesteGroupID
            erFleireEnnToIkkeSvar(calledPhone, request) -> GroupID.Ferdigringt
            else -> null
        }
        nesteGroupID?.nr?.let { databaseUpdater.updateWithResult("CALL sp_updateGroupID($calledPhone, $it)") }
        if (request.modus == Modus.Korona && request.result == Resultat.Svarte) {
            registrerKoronaspesifikkeResultat(request, calledPhone)
        }

        return ResultatFraSamtaleResponse(oppdatert = LocalDateTime.now())
    }

    private fun hypersysIdToPersonId(hypersysId: UserId) = personRepository.find("hypersysID", hypersysId.userId).firstResult<RingbarPerson>().id

    override fun noenRingerTilbake(request: RingerTilbakeRequest): RingbarPerson {
        val callerPhone = personRepository.findById(request.ringerID).phone
        val calledPhone = request.ringtNummer
        val personSomRingerTilbake: RingbarPerson = personRepository.find("phone", calledPhone).firstResult()
        if (entityManager.executeQuery("SELECT 1 FROM v_noenRingerTilbake WHERE phone = '$calledPhone' AND callerPhone = '$callerPhone' LIMIT 1").isEmpty()) {
            throw Exception("Du kan berre registrere å bli ringt opp frå folk du har ringt tidlegare.")
        }
        startSamtale(StartSamtaleRequest(
                ringerID = request.ringerID,
                skalRingesID = personSomRingerTilbake.id
        ))
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

}