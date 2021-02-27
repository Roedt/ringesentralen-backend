package no.roedt.ringesentralen.person

import io.quarkus.hibernate.orm.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PersonRepository : PanacheRepository<Person> {

    fun save(person: Person) {
        if (find("epost", person.email).count() > 0L) {
            update(
                "fornavn = '${person.fornavn}', " +
                        "etternavn = '${person.etternavn}', " +
                        "hypersysID = ${person.hypersysID}," +
                        "telefonnummer = ${person.telefonnummer}," +
                        "postnummer = ${person.postnummer}," +
//TODO: ta stilling til denne                        "rolle = '${person.rolle.name}'," +
                        "lokallag = ${person.lokallag} " +
                        "where epost = '${person.email}'"
            )
        }
        else {
            persist(person)
        }
    }
}