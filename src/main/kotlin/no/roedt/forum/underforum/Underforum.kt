package no.roedt.forum.underforum

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Underforum(
    @JsonProperty("id") val id: String
)
