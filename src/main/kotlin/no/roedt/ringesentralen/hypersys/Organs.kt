package no.roedt.ringesentralen.hypersys

import com.fasterxml.jackson.annotation.JsonProperty

data class Organs(
        @JsonProperty("organs") val organs: List<Organ>
)
