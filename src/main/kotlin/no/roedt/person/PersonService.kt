package no.roedt.person

import io.quarkus.hibernate.orm.panache.PanacheQuery
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class PersonService(private val repository: PersonRepository) {
    fun harMedlemMedHypersysID(hypersysID: Int): Boolean = repository.find("hypersysID", hypersysID).count() > 0
    fun save(oppdatertPerson: Person, kilde: Oppdateringskilde) = repository.save(oppdatertPerson, kilde)
    fun finnFraHypersysId(memberId: Int): PanacheQuery<Person> = repository.find("hypersysID", memberId)
    fun oppdater(oppdatering: PersonOppdatering) = repository.oppdater(oppdatering)
    fun hentMedlemmerILokallag(lokallag: Int): List<Person> = repository.list("lokallag", lokallag)
}
