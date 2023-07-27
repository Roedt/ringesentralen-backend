package no.roedt.ringesentralen.samtale

import jakarta.enterprise.context.Dependent

@Dependent
class OppfoelgingValg21Service(internal val repository: OppfoelgingValg21Repository) {
    fun findById(id: Int): OppfoelgingValg21 = repository.findById(id)
    fun persist(oppfoelgingValg21: OppfoelgingValg21) = repository.persist(oppfoelgingValg21)
}