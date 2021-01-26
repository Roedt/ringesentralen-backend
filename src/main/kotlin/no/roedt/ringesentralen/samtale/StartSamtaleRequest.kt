package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class StartSamtaleRequest(
        @JsonProperty("skalRingesID") val skalRingesID: Long
)