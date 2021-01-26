package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Country(
        @JsonProperty("iso") val iso: String,
        @JsonProperty("name") val name: String
)
