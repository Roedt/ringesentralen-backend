package no.roedt.ringesentralen

import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

@ApplicationScoped
class DatabaseUpdater(val entityManager: EntityManager) {

    fun update(vararg query: String) = query.forEach { toQuery(it).executeUpdate() }

    fun count(query: String): Int = toQuery(query).resultList.size

    fun getResultList(query: String) = toQuery(query).resultList

    fun getSingleResult(query: String) = toQuery(query).singleResult

    private fun toQuery(query: String) = entityManager.createNativeQuery(query)

}