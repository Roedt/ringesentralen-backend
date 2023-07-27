package no.roedt

import jakarta.persistence.EntityManager

fun EntityManager.list(query: String) = this.createNativeQuery(query).resultList
