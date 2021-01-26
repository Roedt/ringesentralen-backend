package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class PostalCode(
        @JsonProperty("name") val name: String,
        @JsonProperty("zip") val zip: String
)
