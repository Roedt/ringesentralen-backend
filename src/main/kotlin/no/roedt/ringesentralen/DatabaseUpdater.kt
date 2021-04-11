package no.roedt.ringesentralen

import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

@ApplicationScoped
class DatabaseUpdater(private val entityManager: EntityManager) {

    fun getResultList(query: String) = entityManager.createNativeQuery(query).resultList

}