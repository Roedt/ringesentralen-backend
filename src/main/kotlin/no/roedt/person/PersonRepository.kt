package no.roedt.person

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import no.roedt.list

@ApplicationScoped
class PersonRepository : PanacheRepositoryBase<Person, Int> {
    fun save(
        person: Person,
        oppdateringskilde: Oppdateringskilde
    ): Long {
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
                    groupID = person.groupID(),
                    oppdateringskilde = oppdateringskilde
                ),
                eksisterendePerson.id
            )
            return eksisterendePerson.id.toLong()
        } else {
            persist(person)
            return person.id.toLong()
        }
    }

    private fun oppdaterEksisterendePerson(
        person: PersonOppdatering,
        id: Int?
    ) {
        try {
            update(
                "fornavn=?1, " +
                    "etternavn=?2, " +
                    "hypersysID=?3, " +
                    "telefonnummer=?4, " +
                    "postnummer=?5, " +
                    "lokallag=?6, " +
                    "groupID=?7, " +
                    "email=?8, " +
                    "sistOppdatert=?9, " +
                    "kilde=?10 " +
                    "where id=?11",
                person.fornavn,
                person.etternavn,
                person.hypersysID,
                person.telefonnummer,
                person.postnummer,
                person.lokallag,
                person.groupID,
                person.email,
                person.sistOppdatert,
                person.kilde,
                id
            )
        } catch (e: Exception) {
            println(
                "Kunne ikke lagre person med hypersysid ${person.hypersysID}, " +
                    "fylke ${person.fylke}, " +
                    "lokallag ${person.lokallag} " +
                    "og postnummer ${person.postnummer} i lokallag ${person.lokallag}"
            )
            throw e
        }
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

    fun hypersysIDTilRingerId(userId: UserId) =
        entityManager.list(
            "select ringer.id from ringer inner join person on person.id = ringer.personId and person.hypersysID = ${userId.userId} "
        ).first() as Int
}
