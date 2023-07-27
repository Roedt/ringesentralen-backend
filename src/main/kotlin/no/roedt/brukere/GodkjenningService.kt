package no.roedt.brukere

import jakarta.enterprise.context.Dependent
import no.roedt.ringesentralen.ringer.Ringer
import java.util.Optional

@Dependent
class GodkjenningService(internal val repository: GodkjenningRepository) {
    fun persist(godkjenning: Godkjenning) = repository.persist(godkjenning)
    fun flyttGodkjenningerTilGeneriskTidligereMedlem(
        tidligereMedlemPerson: Int,
        tidligereMedlemRinger: Ringer,
        personId: Int,
        ikkeMedlemLengerRinger: Optional<Ringer>
    ) {
        repository.update("godkjentPerson=?1 where godkjentPerson=?2", tidligereMedlemPerson, personId)
        ikkeMedlemLengerRinger.ifPresent {
            repository.update("godkjenner=?1 where godkjenner=?2", tidligereMedlemRinger.id, it.id)
        }
    }
}
