package no.roedt.lokallag

import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import no.roedt.postnummer.Postnummer

@ApplicationScoped
class LokallagService(val repository: LokallagRepository) {
    fun findById(lokallag: Int): Lokallag = repository.findById(lokallag)

    fun findAll(): List<Lokallag> = repository.findAll().list()

    fun findAll(ascending: Sort?): List<Lokallag> = repository.findAll(ascending).list()

    fun fromFylke(fylke: Int): List<Lokallag> = repository.fromFylke(fylke)

    fun list(lokallag: Int): List<Lokallag> = repository.list("id", lokallag)

    fun fromPostnummer(postnr: Postnummer): Int = repository.fromPostnummer(postnr)

    fun oppdater(hypersysId: Int, navn: String, id: Int?) = repository.oppdaterNavnOgHypersysID(hypersysId, navn, id)

    fun oppdaterNavn(hypersysId: Int, navn: String) = repository.oppdaterNavn(hypersysId, navn)

    fun oppdaterHypersysID(hypersysId: Int, navn: String) = repository.oppdaterHypersysID(hypersysId, navn)

    fun exists(field: String, value: Any) = repository.exists(field, value)

    fun persist(it: Lokallag) = repository.persist(it)

    fun fromOrganisationName(it: String) = repository.fromOrganisationName(it)

    fun listAll(): List<Lokallag> = repository.listAll()
}
