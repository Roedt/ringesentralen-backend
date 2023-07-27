package no.roedt.kommune

import jakarta.enterprise.context.Dependent

@Dependent
class KommuneService(private val repository: KommuneRepository) {

    fun hent(fylke: Int) = repository
        .list("fylke_id=?1", fylke)
        .mapNotNull { it.lokallag_id }
}
