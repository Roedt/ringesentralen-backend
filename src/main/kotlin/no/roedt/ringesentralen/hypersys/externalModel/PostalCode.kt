package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty

data class PostalCode(
        @JsonProperty("name") val name: String,
        @JsonProperty("zip") val zip: String
)
