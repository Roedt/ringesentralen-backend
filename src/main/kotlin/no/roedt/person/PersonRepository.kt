package no.roedt.person

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import no.roedt.Kilde
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PersonRepository : PanacheRepositoryBase<Person, Int> {

    fun save(person: Person): Long {
        val eksisterendePerson: Person? = finnPerson(person)
        if (eksisterendePerson != null) {
            oppdaterEksisterendePerson(
                PersonOppdatering(
                    telefonnummer = person.telefonnummer,
                    postnummer = person.postnummer,
                    hypersysID = person.hypersysID,
                    kilde = person.kilde,
                    email = person.email,
                    etternavn = person.etternavn,
                    fornavn = person.fornavn,
                    fylke = person.fylke,
                    lokallag = person.lokallag,
                    sistOppdatert = person.sistOppdatert,
                    groupID = person.groupID()
                ),
                eksisterendePerson.id
            )
            return eksisterendePerson.id.toLong()
        } else {
            persist(person)
            return person.id.toLong()
        }
    }

    private fun oppdaterEksisterendePerson(person: PersonOppdatering, id: Int?) {
        val telefonnummer = person.telefonnummer?.let { "'$it'" }
        val kilde =
            if (person.kilde == Kilde.Hypersys || person.kilde == Kilde.Frivillig) ", kilde='${person.kilde}'" else ""
        val postnummer = if (person.postnummer.erUkjent()) "postnummer = ${person.postnummer.Postnummer}," else ""
        val hypersysID = if (person.hypersysID != null) "hypersysID = ${person.hypersysID}, " else ""
        val tid = ZonedDateTime.ofInstant(person.sistOppdatert, ZoneId.of("Europe/Oslo"))
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val oppdatertFraHypersys = if (person.kilde == Kilde.Hypersys) ", sistOppdatert='$tid' " else ""
        update(
            """fornavn = '${person.fornavn}', 
                            etternavn = '${person.etternavn}', 
                            $hypersysID
                            telefonnummer = $telefonnummer,
                            $postnummer
                            lokallag = ${person.lokallag},
                            groupID = ${person.groupID},
                            email = '${person.email}'
                            $oppdatertFraHypersys
                            $kilde
                            where id=$id
                    """
        )
    }

    fun finnPerson(person: Person): Person? =
        find("email=?1", person.email)
            .firstResultOptional<Person>()
            .filter { it.email != "" }
            .orElseGet { find("telefonnummer=?1", person.telefonnummer).firstResultOptional<Person>().orElse(null) }

    fun getPerson(userId: UserId): Person = find("hypersysID", userId.userId).firstResult()
    fun oppdater(oppdatering: PersonOppdatering) {
        oppdaterEksisterendePerson(oppdatering, find("hypersysID=?1", oppdatering.hypersysID).firstResult<Person>().id)
    }
}
