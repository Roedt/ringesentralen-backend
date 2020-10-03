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
            entityManager.createNativeQuery("SELECT v.id FROM v_personerSomKanRinges v")
                    .resultList
                    .firstOrNull()
                    ?.let { it as Int}
                    ?.let { personRepository.findById(it.toLong()) }
}