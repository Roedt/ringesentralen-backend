package no.roedt.ringesentralen.samtale.start

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.person.UserId

@RegisterForReflection
data class StartSamtaleRequest(
    @JsonProperty("skalRingesID") val skalRingesID: Long
)

@RegisterForReflection
data class AutentisertStartSamtaleRequest(
    val userId: UserId,
    val startSamtaleRequest: StartSamtaleRequest,
    val modus: Modus
) {
    fun skalRingesID() = startSamtaleRequest.skalRingesID
}
