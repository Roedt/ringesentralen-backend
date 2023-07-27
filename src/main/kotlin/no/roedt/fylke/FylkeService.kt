package no.roedt.fylke

import jakarta.enterprise.context.Dependent

@Dependent
class FylkeService(private val repository: FylkeRepository) {
    fun findById(fylke: Int): Fylke = repository.findById(fylke)
}
