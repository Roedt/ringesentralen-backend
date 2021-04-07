package no.roedt.ringesentralen.person

import io.quarkus.hibernate.orm.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PersonRepository : PanacheRepository<Person> {

    fun save(person: Person) {
        if (find("email", person.email).count() > 0L || telefonnummerFinsAlt(person)) {
            val telefonnummer = person.telefonnummer?.let { "'$it'"  }
            update(
                """fornavn = '${person.fornavn}', 
                        etternavn = '${person.etternavn}', 
                        hypersysID = ${person.hypersysID}, 
                        telefonnummer = $telefonnummer, 
                        postnummer = ${person.postnummer}, 
                        lokallag = ${person.lokallag},
                        groupID = ${person.groupID},
                        email = '${person.email}'
                        where ((email = '${person.email}') or (telefonnummer != null and telefonnummer = $telefonnummer)) 
                        """
            )
        }
        else {
            persist(person)
        }
    }

    private fun telefonnummerFinsAlt(person: Person) =
        person.telefonnummer != null && find("telefonnummer", person.telefonnummer).count() > 0L
}