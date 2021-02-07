package no.roedt.ringesentralen.innloggaBruker

import UserId
import no.roedt.ringesentralen.PersonRepository
import no.roedt.ringesentralen.samtale.RingbarPerson
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class InnloggaBrukerService(private val personRepository: PersonRepository) {
    fun getProfil(userId: UserId): RingbarPerson = personRepository.find("hypersysID", userId.userId).firstResult()
}