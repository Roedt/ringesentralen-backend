package no.roedt.ringesentralen

import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

@ApplicationScoped
class DatabaseUpdater(val entityManager: EntityManager) {

    fun updateNoTran(vararg query: String) {
        query.forEach { entityManager.createNativeQuery(it).executeUpdate() }
    }

    fun updateWithResult(query: String): MutableList<Any?>? = getResultList(query)

    fun count(query: String): Int = entityManager.createNativeQuery(query).resultList.size

    fun getResultList(query: String) = entityManager.createNativeQuery(query).resultList

    fun getSingleResult(query: String) = entityManager.createNativeQuery(query).singleResult

}