package no.roedt.ringesentralen.samtale

import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.PersonRepository
import java.time.LocalDateTime
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

interface RingService {
    fun hentNestePersonAaRinge(nestePersonAaRingeRequest: Int): RingbarPerson?
    fun startSamtale(request: StartSamtaleRequest): StartSamtaleResponse
    fun registrerResultatFraSamtale(resultatFraSamtaleRequest: ResultatFraSamtaleRequest): ResultatFraSamtaleResponse
}

@ApplicationScoped
class RingServiceBean(
        val personRepository: PersonRepository,
        val entityManager: EntityManager
): RingService {

    override fun hentNestePersonAaRinge(nestePersonAaRingeRequest: Int): RingbarPerson? =
            entityManager
                    .createNativeQuery("SELECT v.id FROM v_personerSomKanRinges v WHERE lokallag = '$nestePersonAaRingeRequest'")
                    .resultList
                    .firstOrNull()
                    ?.let { it as Int}
                    ?.let { personRepository.findById(it.toLong()) }

    override fun startSamtale(request: StartSamtaleRequest): StartSamtaleResponse {
        val callerPhone = personRepository.findById(request.ringerID).phone
        val calledPhone = personRepository.findById(request.skalRingesID).phone
        entityManager.executeQuery("CALL sp_startSamtale($calledPhone, $callerPhone)")
        return StartSamtaleResponse(request.ringerID, request.skalRingesID, LocalDateTime.now())
    }

    override fun registrerResultatFraSamtale(request: ResultatFraSamtaleRequest): ResultatFraSamtaleResponse {
        /*
	function ringtToGangerTidligereUtenSvarOgIngenAndreResponser($mysqli, $calledPhone) {
		$sql =
		$resultsQuery = mysqli_query($mysqli, "select 1 from `call` where calledPhone = $calledPhone and result = 0");
		$antallIkkeSvar = mysqli_num_rows($resultsQuery);
		$antallSvarQuery = mysqli_query($mysqli, "select 1 from `call` where calledPhone = $calledPhone and result != 0 and result != 9");
		$antallSvar = mysqli_num_rows($antallSvarQuery);

		return $antallIkkeSvar > 2 && $antallSvar == 0;
	}
	function updatePersonsGroupIDAndMaybeNeedsFollowup($mysqli, $result, $calledPhone) {
		elseif ($result == 0 && ringtToGangerTidligereUtenSvarOgIngenAndreResponser($mysqli, $calledPhone)) {
			mysqli_query($mysqli, "CALL sp_updateGroupID($calledPhone, 2)");
		}
	}
         */
        val callerPhone = personRepository.findById(request.ringerID).phone
        val calledPhone = personRepository.findById(request.ringtID).phone
        assert(request.result in request.modus.gyldigeResultattyper)
        entityManager.executeQuery("CALL sp_registrerSamtale($calledPhone, $callerPhone, ${request.result.nr}, '${request.kommentar}')")
        request.result.nesteGroupID?.nr?.let { entityManager.executeQuery("CALL sp_updateGroupID($calledPhone, $it)") }
        if (request.modus == Modus.Korona && request.result == Resultat.Svarte) {
            registrerKoronaspesifikkeResultat(request, calledPhone)
        }

        return ResultatFraSamtaleResponse(oppdatert = LocalDateTime.now())
    }

    private fun registrerKoronaspesifikkeResultat(request: ResultatFraSamtaleRequest, calledPhone: String) {
        val resultat: KoronaspesifikkeResultat = request.modusspesifikkeResultat as KoronaspesifikkeResultat
        entityManager.executeQuery("CALL sp_registrerOppfoelgingKorona($calledPhone, ${resultat.vilHaKoronaprogram}, ${resultat.vilBliMerAktiv}, ${resultat.vilHaValgkampsbrev}, ${request.vilIkkeBliRingt})");
    }

    fun EntityManager.executeQuery(query: String) = entityManager.createNativeQuery(query).resultList

}