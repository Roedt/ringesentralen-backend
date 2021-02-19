package no.roedt.ringesentralen

import com.arjuna.ats.jta.TransactionManager
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

@ApplicationScoped
class DatabaseUpdater(val entityManager: EntityManager) {

    fun update(vararg query: String) {
        val transactionManager = TransactionManager.transactionManager()
        transactionManager.begin()
        query.forEach { entityManager.createNativeQuery(it).executeUpdate() }
        transactionManager.commit()
    }

    fun updateWithResult(query: String): MutableList<Any?>? {
        val transactionManager = TransactionManager.transactionManager()
        transactionManager.begin()
        val results = getResultList(query)
        transactionManager.commit()
        return results
    }

    fun count(query: String): Int = entityManager.createNativeQuery(query).resultList.size

    fun getResultList(query: String) = entityManager.createNativeQuery(query).resultList

    fun getSingleResult(query: String) = entityManager.createNativeQuery(query).singleResult

}