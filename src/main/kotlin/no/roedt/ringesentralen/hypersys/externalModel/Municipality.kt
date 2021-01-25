package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Municipality(
        @JsonProperty("county") val county: County,
        @JsonProperty("identifier") val identifier: String,
        @JsonProperty("name") val name: String
)