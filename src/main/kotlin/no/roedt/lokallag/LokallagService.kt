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
    fun oppdater(hypersysId: Int, navn: String, id: Int?) =
        repository.update("hypersysID=?1, navn=?2 where id=?3", hypersysId, navn, id)

    fun oppdaterNavn(hypersysId: Int, navn: String) =
        repository.update("navn=?1 where hypersysID=?2", navn, hypersysId)

    fun oppdaterHypersysID(hypersysId: Int, navn: String) =
        repository.update("hypersysID=?1 where navn=?2", hypersysId, navn)

    fun exists(field: String, value: Any) = repository.find(field, value).count() > 0
    fun persist(it: Lokallag) = repository.persist(it)
    fun fromOrganisationName(it: String) = repository.fromOrganisationName(it)
    fun listAll(): List<Lokallag> = repository.listAll()
}
