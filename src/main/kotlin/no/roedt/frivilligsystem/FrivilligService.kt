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
import no.roedt.ringesentralen.Roles
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
    fun hentAlle(userId: UserId, roller: Set<String>) = hentFrivilligeUtFraMinRolle(roller, personRepository.getPerson(userId))
        .map { FrivilligResponse(
            frivillig = it,
            person = personRepository.findById(it.personId.toLong()),
            aktiviteter = aktivitetForFrivilligRepository.list("frivillig_id", it.id)
        ) }

    private fun hentFrivilligeUtFraMinRolle(roller: Set<String>, ringer: Person): List<Frivillig> = when {
        roller.contains(Roles.admin) -> frivilligRepository.listAll()
        roller.contains(Roles.godkjenner) -> {
            databaseUpdater.getResultList("select f.id from frivillig f inner join person p on p.id = f.personId and p.fylke=${ringer.fylke} ORDER BY p.etternavn ASC")
                .map { it as Int }
                .map { it.toLong() }
                .map { frivilligRepository.findById(it) }
        }
        else -> databaseUpdater.getResultList("select f.id from frivillig f inner join person p on p.id = f.personId and p.lokallag=${ringer.lokallag} ORDER BY p.etternavn ASC")
            .map { it as Int }
            .map { it.toLong() }
            .map { frivilligRepository.findById(it) }
    }

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