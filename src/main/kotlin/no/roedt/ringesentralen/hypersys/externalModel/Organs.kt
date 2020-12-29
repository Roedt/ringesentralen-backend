package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty

data class Organs(
        @JsonProperty("organs") val organs: List<Organ>
)
