package no.roedt.ringesentralen.innloggaBruker

import no.roedt.ringesentralen.brukere.FylkeRepository
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
    fun getProfil(userId: UserId): Profil = personRepository.find("hypersysID", userId.userId).firstResult<Person>().toProfil()

private fun Person.toProfil(): Profil = Profil(
    hypersysID = hypersysID,
    fornavn = fornavn,
    etternavn = etternavn,
    telefonnummer = telefonnummer,
    email = email,
    postnummer = postnummer,
    fylke = fylkeRepository.findById(fylke).navn,
    lokallag = lokallagRepository.findById(lokallag.toLong()).navn,
    rolle = GroupID.getRoller(groupID)
)
}
