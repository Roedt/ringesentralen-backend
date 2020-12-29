package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty

data class Municipality(
        @JsonProperty("county") val county: County,
        @JsonProperty("identifier") val identifier: String,
        @JsonProperty("name") val name: String
)