package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty
import no.roedt.ringesentralen.Lokallag

data class NestePersonAaRingeRequest(
        @JsonProperty("lokallag") val lokallag: Lokallag
)