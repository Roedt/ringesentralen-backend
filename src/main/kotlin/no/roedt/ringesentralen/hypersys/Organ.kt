package no.roedt.ringesentralen.hypersys

import com.fasterxml.jackson.annotation.JsonProperty

data class Organ(
        @JsonProperty("") val id: Int,
        @JsonProperty("name") val name: String,
        @JsonProperty("organ_type") val organ_type: String
)