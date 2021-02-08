package no.roedt.ringesentralen.innloggaBruker

import no.roedt.ringesentralen.person.UserId
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.Person
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class InnloggaBrukerService(private val personRepository: PersonRepository) {
    fun getProfil(userId: UserId): Person = personRepository.find("hypersysID", userId.userId).firstResult()
}