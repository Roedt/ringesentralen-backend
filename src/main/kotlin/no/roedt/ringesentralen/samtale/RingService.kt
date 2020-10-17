package no.roedt.ringesentralen.samtale

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
        entityManager
                .createNativeQuery("CALL sp_startSamtale($calledPhone, $callerPhone)")
                .resultList
        return StartSamtaleResponse(request.ringerID, request.skalRingesID, LocalDateTime.now())
    }

    override fun registrerResultatFraSamtale(request: ResultatFraSamtaleRequest): ResultatFraSamtaleResponse {
        /*
        if (mysqli_query($mysqli, "CALL sp_registrerSamtale($calledPhone, $callerPhone, $result, '$comment')")) {
				updatePersonsGroupIDAndMaybeNeedsFollowup($mysqli, $result, $calledPhone);
				if ($modus == Modus::korona && $result == 11) {
					$koronaprogram = nullIfUndefined('koronaprogram', 0);
					$merAktiv = nullIfUndefined('merAktiv', 0);
					$valgkampsbrev = nullIfUndefined('valgkampsbrev', 0);
					$vilIkkeBliRingt = nullIfUndefined('vilIkkeBliRingt', 0);
					mysqli_query($mysqli, "CALL sp_registrerOppfoelgingKorona($calledPhone, $koronaprogram, $merAktiv, $valgkampsbrev, $vilIkkeBliRingt)");
				}
				else if ($modus == Modus::ekstern && $result != 2 && result != 4) {
					$hasActuallyReceived = nullIfUndefined('hasActuallyReceived', 0);
					$stopSubscription = nullIfUndefined('stopSubscription', 0);
					$smsReminder = nullIfUndefined('smsReminder', 0);
					mysqli_query($mysqli, "CALL sp_registrerOppfoelgingEkstern($calledPhone, $hasActuallyReceived, $stopSubscription, $smsReminder)");
				}
				header('Location: ../dashboard/dashboard.php');

         */
        val callerPhone = personRepository.findById(request.ringerID).phone
        val calledPhone = personRepository.findById(request.ringtID).phone
        entityManager.executeQuery("CALL sp_registrerSamtale($calledPhone, $callerPhone, ${request.result}, '${request.comment}')")
        entityManager.executeQuery("CALL sp_registrerSamtale($calledPhone, $callerPhone, ${request.result}, '${request.comment}')")


        return ResultatFraSamtaleResponse(oppdatert = LocalDateTime.now())
    }

    fun EntityManager.executeQuery(query: String) = entityManager.createNativeQuery(query).resultList

}