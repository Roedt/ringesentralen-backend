package no.roedt.ringesentralen.person

import io.quarkus.hibernate.orm.panache.PanacheRepository
import no.roedt.ringesentralen.Kilde
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PersonRepository : PanacheRepository<Person> {

    fun save(person: Person) {
        if (find("email", person.email).count() > 0L || telefonnummerFinsAlt(person)) {
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
                        where ((email = '${person.email}') or (telefonnummer is not null null and telefonnummer = $telefonnummer)) 
                        """
            )
        }
        else {
            persist(person)
        }
    }

    fun getPerson(userId: UserId): Person = find("hypersysID", userId.userId).firstResult()

    private fun telefonnummerFinsAlt(person: Person) =
        person.telefonnummer != null && find("telefonnummer", person.telefonnummer).count() > 0L
}