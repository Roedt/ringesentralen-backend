package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.UserId

@RegisterForReflection
data class StartSamtaleRequest(
        @JsonProperty("skalRingesID") val skalRingesID: Long
)

@RegisterForReflection
data class AutentisertStartSamtaleRequest(val userId: UserId, val startSamtaleRequest: StartSamtaleRequest) {
        fun skalRingesID() = startSamtaleRequest.skalRingesID
}