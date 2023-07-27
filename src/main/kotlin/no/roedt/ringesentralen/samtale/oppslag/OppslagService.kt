package no.roedt.ringesentralen.samtale.oppslag

import jakarta.enterprise.context.Dependent
import no.roedt.person.Person

@Dependent
class OppslagService(internal val repository: OppslagRepository) {
    fun flyttTilGeneriskTidligereMedlem(tidligereMedlemPerson: Person, personId: Int, ikkeMedlemLenger: Person) {
        repository.update("ringt=?1 where ringt=?2", tidligereMedlemPerson.id, personId)
        repository.update(
            "ringerHypersysId=?1 where ringerHypersysId=?2",
            tidligereMedlemPerson.hypersysID,
            ikkeMedlemLenger.hypersysID
        )
    }

    fun persist(oppslag: Oppslag) = repository.persist(oppslag)
}
