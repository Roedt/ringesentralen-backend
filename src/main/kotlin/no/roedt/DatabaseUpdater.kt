package no.roedt

import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.EntityManager

@ApplicationScoped
class DatabaseUpdater(private val entityManager: EntityManager) {

    fun getResultList(query: String) = entityManager.createNativeQuery(query).resultList
}

fun EntityManager.list(query: String) = this.createNativeQuery(query).resultList