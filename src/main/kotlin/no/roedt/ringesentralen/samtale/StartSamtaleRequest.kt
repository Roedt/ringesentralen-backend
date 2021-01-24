package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class StartSamtaleRequest(
        @JsonProperty("ringerID") var ringerID: Long,
        @JsonProperty("skalRingesID") var skalRingesID: Long
)