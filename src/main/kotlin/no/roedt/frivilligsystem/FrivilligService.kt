package no.roedt.frivilligsystem

import no.roedt.frivilligsystem.kontakt.AutentisertRegistrerKontaktRequest
import no.roedt.frivilligsystem.kontakt.Kontakt
import no.roedt.frivilligsystem.kontakt.KontaktRepository
import no.roedt.frivilligsystem.registrer.AktivitetForFrivillig
import no.roedt.frivilligsystem.registrer.AktivitetForFrivilligRepository
import no.roedt.frivilligsystem.registrer.AutentisertRegistrerNyFrivilligRequest
import no.roedt.frivilligsystem.registrer.RegistrerNyFrivilligRequest
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Kilde
import no.roedt.ringesentralen.brukere.FylkeRepository
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FrivilligService(
    val frivilligRepository: FrivilligRepository,
    val personRepository: PersonRepository,
    val kontaktRepository: KontaktRepository,
    val databaseUpdater: DatabaseUpdater,
    val lokallagRepository: LokallagRepository,
    val fylkeRepository: FylkeRepository,
    val aktivitetForFrivilligRepository: AktivitetForFrivilligRepository
) {
    fun hentAlle(userId: UserId): List<Frivillig> = frivilligRepository.findAll().list()

    fun registrerNyFrivillig(autentisertRequest: AutentisertRegistrerNyFrivilligRequest): Frivillig {
        val request = autentisertRequest.request
        val person = request.toPerson()
        personRepository.save(person)
        val frivillig = Frivillig(
            personId = person.id.toInt(),
            alleredeAktivILokallag = request.alleredeAktivILokallag,
            medlemIRoedt = request.medlemIRoedt,
            spesiellKompetanse = request.spesiellKompetanse,
            andreTingDuVilBidraMed = request.andreTingDuVilBidraMed,
            fortellLittOmDegSelv = request.fortellLittOmDegSelv,
        )
        frivilligRepository.persist(frivillig)
        request.kanTenkeSegAaBidraMedAktiviteter
            .map { AktivitetForFrivillig(frivillig_id = frivillig.id, aktivitet = it) }
            .forEach { aktivitetForFrivilligRepository.persist(it) }

        return frivillig
    }


    private fun RegistrerNyFrivilligRequest.toPerson(): Person {
        val lokallag = lokallagRepository.fromPostnummer(postnummer)
        return Person(
            hypersysID = null,
            fornavn = fornavn,
            etternavn = etternavn,
            telefonnummer = telefonnummer,
            email = null,
            postnummer = postnummer,
            fylke = fylkeRepository.getFylke(lokallag, postnummer),
            lokallag = lokallag,
            groupID = 0,
            kilde = Kilde.Frivillig,
            iperID = null
        )
    }

    fun registrerKontakt(request: AutentisertRegistrerKontaktRequest) =
        kontaktRepository.persist(
            Kontakt(
                frivillig_id = request.request.frivillig_id,
                tilbakemelding = request.request.tilbakemelding,
                registrert_av = personRepository.find("hypersysID", request.userId.userId).firstResult<Person>().id
            )
        )
}