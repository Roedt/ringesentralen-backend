package no.roedt.ringesentralen.brukere

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.person.UserId

@RegisterForReflection
data class TilgangsendringsRequest(
        @JsonProperty("personMedEndraTilgang") val personMedEndraTilgang: Long
)

@RegisterForReflection
data class AutentisertTilgangsendringRequest(val userId: UserId, val tilgangsendringRequest: TilgangsendringsRequest) {
        fun personMedEndraTilgang() = tilgangsendringRequest.personMedEndraTilgang
}
