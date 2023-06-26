package no.roedt.innloggaBruker

import jakarta.enterprise.context.ApplicationScoped
import no.roedt.brukere.FylkeRepository
import no.roedt.brukere.GenerellRolle
import no.roedt.lokallag.Lokallag
import no.roedt.lokallag.LokallagRepository
import no.roedt.person.Person
import no.roedt.person.PersonRepository
import no.roedt.person.UserId
import no.roedt.ringesentralen.RingespesifikkRolle
import no.roedt.ringesentralen.brukere.RingesentralenGroupID
import no.roedt.token.TokenService
import java.util.Optional

@ApplicationScoped
class InnloggaBrukerService(
    private val personRepository: PersonRepository,
    private val fylkeRepository: FylkeRepository,
    private val lokallagRepository: LokallagRepository,
    private val tokenService: TokenService
) {
    fun getProfil(userId: UserId): Profil? = getPerson(userId).map { it.toProfil() }.orElse(null)

    private fun getPerson(userId: UserId): Optional<Person> =
        personRepository.find("hypersysID", userId.userId).firstResultOptional()

    private fun Person.toProfil(): Profil = Profil(
        hypersysID = hypersysID,
        fornavn = fornavn,
        etternavn = etternavn,
        telefonnummer = telefonnummer,
        email = email,
        postnummer = postnummer.Postnummer,
        fylke = fylke,
        lokallag = lokallag,
        rolle = RingesentralenGroupID.from(groupID()).roller,
        fylkeNavn = fylkeRepository.findById(fylke).navn,
        lokallagNavn = lokallagRepository.findById(lokallag).navn
    )

    fun getLokallag(userId: UserId, groups: Set<String>): List<Lokallag> = when {
        groups.contains(GenerellRolle.admin) -> lokallagRepository.findAll().list()
        groups.contains(RingespesifikkRolle.godkjenner) -> lokallagRepository.fromFylke(
            fylkeRepository.getFylkeIdFraLokallag(
                getPerson(userId).get().lokallag
            )
        )

        getProfil(userId) == null -> listOf()
        else -> listOf(lokallagRepository.findById(getPerson(userId).get().lokallag))
    }

    fun getRoller(userId: UserId): Set<String> = tokenService.hentRoller(userId)
}
