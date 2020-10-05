package no.roedt.ringesentralen.samtale

import no.roedt.ringesentralen.PersonRepository
import java.time.LocalDateTime
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

interface RingService {
    fun hentNestePersonAaRinge(nestePersonAaRingeRequest: Int): RingbarPerson?
    fun startSamtale(startSamtaleRequest: StartSamtaleRequest): StartSamtaleResponse
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
}