package no.roedt.frivilligsystem

import no.roedt.frivilligsystem.kontakt.AutentisertRegistrerKontaktRequest
import no.roedt.frivilligsystem.kontakt.Kontakt
import no.roedt.frivilligsystem.kontakt.KontaktRepository
import no.roedt.frivilligsystem.registrer.RegistrerNyFrivilligRequest
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Kilde
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FrivilligService(
    val frivilligRepository: FrivilligRepository,
    val personRepository: PersonRepository,
    val kontaktRepository: KontaktRepository,
    val databaseUpdater: DatabaseUpdater
) {
    fun hentAlle(userId: UserId): List<Frivillig> = frivilligRepository.findAll().list()

    fun registrerNyFrivillig(request: RegistrerNyFrivilligRequest): Frivillig {
        val person = request.toPerson()
        personRepository.save(person)
        val frivillig = Frivillig(
            alleredeAktivILokallag = request.alleredeAktivILokallag,
            medlemIRoedt = request.medlemIRoedt,
            //     kanTenkeSegAaBidraMedAktiviteter = request.kanTenkeSegAaBidraMedAktiviteter,
            spesiellKompetanse = request.spesiellKompetanse,
            andreTingDuVilBidraMed = request.andreTingDuVilBidraMed,
            fortellLittOmDegSelv = request.fortellLittOmDegSelv,
        )
            .apply {
                this.person = personRepository.find("epost", person.email).firstResult()
            }
        frivilligRepository.persist(frivillig)
        return frivillig
    }

    private fun RegistrerNyFrivilligRequest.toPerson() = Person(
        hypersysID = null,
        fornavn = fornavn,
        etternavn = etternavn,
        telefonnummer = telefonnummer,
        email = null,
        postnummer = postnummer.getPostnummer(),
        fylke = -1,
        lokallag = getLokallagFraPostnummer(postnummer) ?: 0,
        groupID = 0,
        kilde = Kilde.Verva,
        iperID = null
//        rolle = Rolle.frivillig
    )

    private fun getLokallagFraPostnummer(postnummer: PostnummerDTO): Int? =
        toLokallagId("select lokallag from postnummerIKommunerMedFleireLag where postnummerFra =< ${postnummer.postnummer} and postnummerTil >= ${postnummer.postnummer}")
            ?: toLokallagId("select l.id from lokallag l inner join kommune k on k.lokallag_id = l.id inner join postnummer  p on p.kommunekode = k.nummer where p.postnummer = ${postnummer.postnummer}")

    private fun toLokallagId(query: String) = databaseUpdater.getResultList(query)
        .map { it as Array<*> }
        .map { it[0] as Long }
        .map { it.toInt() }
        .firstOrNull()

    fun registrerKontakt(request: AutentisertRegistrerKontaktRequest) =
        kontaktRepository.persist(
            Kontakt(
                frivillig_id = request.request.frivillig_id,
                tilbakemelding = request.request.tilbakemelding,
                registrert_av = personRepository.find("hypersysID", request.userId.userId).firstResult<Person>().id
            )
        )
}