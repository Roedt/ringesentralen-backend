package no.roedt.postnummer

import jakarta.enterprise.context.Dependent

@Dependent
class PostnummerService(private val repository: PostnummerRepository) {
    fun findById(postnummer: String): Postnummer = repository.findById(postnummer)
    fun ukjent(): Postnummer = findById("-1")
}