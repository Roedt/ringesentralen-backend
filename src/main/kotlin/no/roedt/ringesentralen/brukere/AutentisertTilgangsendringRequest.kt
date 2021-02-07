package no.roedt.ringesentralen.brukere

import no.roedt.ringesentralen.UserId
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class AutentisertTilgangsendringRequest(val userId: UserId, val tilgangsendringRequest: TilgangsendringsRequest) {
    fun personMedEndraTilgang() = tilgangsendringRequest.personMedEndraTilgang
}