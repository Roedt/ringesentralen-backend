package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty

data class County (
        @JsonProperty("country") val country: Country,
        @JsonProperty("name") val name: String
)
