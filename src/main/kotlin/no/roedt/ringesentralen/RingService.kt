package no.roedt.ringesentralen

import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

interface RingService {
    fun hentNestePersonAaRinge(): RingbarPerson?
}

@ApplicationScoped
class RingServiceBean(
        val personRepository: PersonRepository,
        val entityManager: EntityManager
): RingService {

    override fun hentNestePersonAaRinge(): RingbarPerson? =
            entityManager.createNamedQuery("RingbarPerson.finnNestePersonAaRinge", Long::class.javaObjectType)
                    .resultList
                    .firstOrNull()
                    ?.let { personRepository.findById(it) }
}