package no.roedt.ringesentralen

import com.arjuna.ats.jta.TransactionManager
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

@ApplicationScoped
class DatabaseUpdater(val entityManager: EntityManager) {

    fun update(query: String) {
        TransactionManager.transactionManager().begin()
        entityManager.createNativeQuery(query).executeUpdate()
        TransactionManager.transactionManager().commit()
    }

    fun updateWithResult(query: String): MutableList<Any?>? {
        TransactionManager.transactionManager().begin()
        val results = entityManager.createNativeQuery(query).resultList
        TransactionManager.transactionManager().commit()
        return results
    }
}