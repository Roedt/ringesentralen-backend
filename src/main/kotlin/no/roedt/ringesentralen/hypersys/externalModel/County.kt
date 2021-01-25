package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class County (
        @JsonProperty("country") val country: Country,
        @JsonProperty("name") val name: String
)
