package no.roedt.ringesentralen.innloggaBruker

import no.roedt.ringesentralen.Roles
import no.roedt.ringesentralen.brukere.FylkeRepository
import no.roedt.ringesentralen.lokallag.Lokallag
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class InnloggaBrukerService(
    private val personRepository: PersonRepository,
    private val fylkeRepository: FylkeRepository,
    private val lokallagRepository: LokallagRepository
) {
    fun getProfil(userId: UserId): Profil = getPerson(userId).toProfil()

    private fun getPerson(userId: UserId) =
        personRepository.find("hypersysID", userId.userId).firstResult<Person>()

    private fun Person.toProfil(): Profil = Profil(
        hypersysID = hypersysID,
        fornavn = fornavn,
        etternavn = etternavn,
        telefonnummer = telefonnummer,
        email = email,
        postnummer = postnummer,
        fylke = fylke,
        lokallag = lokallag,
        rolle = GroupID.from(groupID).roller,
        fylkeNavn = fylkeRepository.findById(fylke).navn,
        lokallagNavn = lokallagRepository.findById(lokallag.toLong()).navn
    )

    fun getLokallag(userId: UserId, groups: Set<String>): List<Lokallag> {
        val person = getPerson(userId)
        if (groups.contains(Roles.admin)) {
            return lokallagRepository.findAll().list()
        }
        else if (groups.contains(Roles.godkjenner)) {
            val fylkeFraLokallag = fylkeRepository.getFylkeIdFraLokallag(person.lokallag)
            return lokallagRepository.fromFylke(fylkeFraLokallag)
        }
        return listOf(lokallagRepository.findById(person.lokallag.toLong()))
    }
}
