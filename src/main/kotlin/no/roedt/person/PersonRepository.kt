package no.roedt.person

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import no.roedt.Kilde
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PersonRepository : PanacheRepositoryBase<Person, Int> {

    fun save(person: Person): Long {
        val eksisterendePerson: Person? = finnPerson(person)
        if (eksisterendePerson != null) {
            val telefonnummer = person.telefonnummer?.let { "'$it'" }
            val kilde =
                if (person.kilde == Kilde.Hypersys || person.kilde == Kilde.Frivillig) ", kilde='${person.kilde}'" else ""
            val postnummer = if (person.postnummer != -1) "postnummer = ${person.postnummer}," else ""
            val hypersysID = if (person.hypersysID != null) "hypersysID = ${person.hypersysID}, " else ""
            update(
                """fornavn = '${person.fornavn}', 
                        etternavn = '${person.etternavn}', 
                        $hypersysID
                        telefonnummer = $telefonnummer,
                        $postnummer
                        lokallag = ${person.lokallag},
                        groupID = ${person.groupID()},
                        email = '${person.email}'
                        $kilde
                        where id=${eksisterendePerson.id}
                """
            )
            return eksisterendePerson.id.toLong()
        } else {
            persist(person)
            return person.id.toLong()
        }
    }

    fun finnPerson(person: Person): Person? =
        find("email=?1", person.email)
            .firstResultOptional<Person>()
            .filter { it.email != "" }
            .orElseGet { find("telefonnummer=?1", person.telefonnummer).firstResultOptional<Person>().orElse(null) }

    fun getPerson(userId: UserId): Person = find("hypersysID", userId.userId).firstResult()
}