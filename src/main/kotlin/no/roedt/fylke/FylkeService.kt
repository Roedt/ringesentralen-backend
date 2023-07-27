package no.roedt.fylke

import jakarta.enterprise.context.Dependent
import no.roedt.person.Postnummer

@Dependent
class FylkeService(private val repository: FylkeRepository) {
    fun findById(fylke: Int): Fylke = repository.findById(fylke)
    fun toFylke(postnummer: Postnummer): Int = repository.toFylke(postnummer)
    fun getFylkeIdFraLokallag(lokallag: Int): Int = repository.getFylkeIdFraLokallag(lokallag)
    fun getFylke(lokallag: Int, postnummer: Postnummer): Int = repository.getFylke(lokallag, postnummer)
}
