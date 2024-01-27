package no.roedt.person

import io.quarkus.hibernate.orm.panache.PanacheQuery
import jakarta.enterprise.context.ApplicationScoped
import no.roedt.Kilde
import no.roedt.postnummer.Postnummer
import no.roedt.ringesentralen.brukere.RingesentralenGroupID
import java.util.Optional

@ApplicationScoped
class PersonService(internal val repository: PersonRepository) {
    fun harMedlemMedHypersysID(hypersysID: Int): Boolean = repository.find("hypersysID", hypersysID).count() > 0

    fun save(
        oppdatertPerson: Person,
        kilde: Oppdateringskilde
    ) = repository.save(oppdatertPerson, kilde)

    fun finnFraHypersysId(memberId: Int): PanacheQuery<Person> = repository.find("hypersysID", memberId)

    fun oppdater(oppdatering: PersonOppdatering) = repository.oppdater(oppdatering)

    fun hentMedlemmerILokallag(lokallag: Int): List<Person> =
        repository.list("lokallag", lokallag)
            .filterNot { it.hypersysID == null }
            .filterNot { it.kilde == Kilde.Systembruker }

    fun finnFraNavn(
        fornavn: String,
        etternavn: String
    ): Person = repository.find("fornavn=?1 and etternavn=?2", fornavn, etternavn).firstResult()

    fun deleteById(personId: Int) = repository.deleteById(personId)

    fun findById(personId: Int): Person = repository.findById(personId)

    fun getPerson(userId: UserId): Person = repository.getPerson(userId)

    fun finnPerson(person: Person): Person? = repository.finnPerson(person)

    fun finnFraTelefonnummer(telefonnummer: String?): Optional<Person> =
        repository.find("telefonnummer", telefonnummer).firstResultOptional()

    fun finnFraEpost(email: String?): PanacheQuery<Person> = repository.find("email", email)

    fun listBrukere(
        filtrer: Boolean,
        brukerId: UserId
    ): List<Person> {
        val brukersFylke = finnFraHypersysId(brukerId.userId).firstResult<Person>().fylke
        val filtrerPaaFylke = if (filtrer) "" else "and fylke=$brukersFylke"
        return repository
            .list(
                "(groupID=?1 or groupID=?2 or groupID=?3 or groupID=?4 or groupID=?5 or groupID=?6) $filtrerPaaFylke",
                RingesentralenGroupID.UgodkjentRinger.nr,
                RingesentralenGroupID.AvslaattRinger.nr,
                RingesentralenGroupID.GodkjentRinger.nr,
                RingesentralenGroupID.GodkjentRingerMedlemmer.nr,
                RingesentralenGroupID.LokalGodkjenner.nr,
                RingesentralenGroupID.Admin.nr
            )
            .filter { !it.isSystembruker() }
    }

    fun oppdaterRolle(
        nyRolle: Int,
        person: Int
    ) = repository.update("groupID=?1 where id=?2", nyRolle, person)

    fun systembruker(): Person = repository.find("fornavn='Systembruker' and etternavn='Frontend'").firstResult()

    fun oppdaterNavnFraHypersys(
        firstName: String,
        lastName: String,
        nyttPostnr: Postnummer,
        hypersysID: Int?
    ) = repository.update(
        "fornavn = ?1, etternavn = ?2, postnummer = ?3 where hypersysID = ?4",
        firstName,
        lastName,
        nyttPostnr,
        hypersysID
    )

    fun persist(person: Person) = repository.persist(person)

    fun oppdaterRolleFraTelefonnummer(
        nyRolle: Int,
        telefonnummer: String
    ) = repository.update("groupID=?1 where telefonnummer=?2", nyRolle, telefonnummer)

    fun hypersysIDTilRingerId(userId: UserId) = repository.hypersysIDTilRingerId(userId)
}
