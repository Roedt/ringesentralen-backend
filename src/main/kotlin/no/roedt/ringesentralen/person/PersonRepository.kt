package no.roedt.ringesentralen.person

import io.quarkus.hibernate.orm.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PersonRepository : PanacheRepository<Person> {

    fun save(person: Person) {
        if (find("email", person.email).count() > 0L) {
            val telefonnummer = person.telefonnummer?.let { "'$it'"  }
            update(
                """fornavn = '${person.fornavn}', 
                        etternavn = '${person.etternavn}', 
                        hypersysID = ${person.hypersysID}, 
                        telefonnummer = $telefonnummer, 
                        postnummer = ${person.postnummer}, 
                        lokallag = ${person.lokallag} 
                        where email = '${person.email}'
                        """
            )
        }
        else {
            persist(person)
        }
    }
}