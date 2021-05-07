package no.roedt.ringesentralen.person

import io.quarkus.hibernate.orm.panache.PanacheRepository
import no.roedt.ringesentralen.Kilde
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PersonRepository : PanacheRepository<Person> {

    fun save(person: Person): Person {
        if (find("email", person.email).count() > 0L || telefonnummerFinsAlt(person)) {
            val eksisterendePerson: Person = find("email=?1", person.email).firstResultOptional<Person>()
                .orElseGet { find("telefonnummer=?1", person.telefonnummer).firstResult() }
            val telefonnummer = person.telefonnummer?.let { "'$it'"  }
            val kilde = if (person.kilde == Kilde.Hypersys) ", kilde='${person.kilde}'" else ""
            val postnummer = if (person.postnummer != -1) "postnummer = ${person.postnummer}," else ""
            update(
                """fornavn = '${person.fornavn}', 
                        etternavn = '${person.etternavn}', 
                        hypersysID = ${person.hypersysID}, 
                        telefonnummer = $telefonnummer,
                        $postnummer
                        lokallag = ${person.lokallag},
                        groupID = ${person.groupID},
                        email = '${person.email}'
                        $kilde
                        where id=${eksisterendePerson.id}
                        """
            )
            return eksisterendePerson
        }
        else {
            persist(person)
            return person
        }
    }


    fun getPerson(userId: UserId): Person = find("hypersysID", userId.userId).firstResult()

    private fun telefonnummerFinsAlt(person: Person) =
        person.telefonnummer != null && find("telefonnummer", person.telefonnummer).count() > 0L
}