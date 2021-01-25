package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Address(
        @JsonProperty("id") val id: Int,
        @JsonProperty("name") val name: String,
        @JsonProperty("address1") val address1: String,
        @JsonProperty("address2") val address2: String,
        @JsonProperty("subject") val subject: String, // Kanskje eigentleg ein enum?
        @JsonProperty("postal_code") val postalCode: List<String>
)
