package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty
import no.roedt.ringesentralen.Lokallag
import no.roedt.ringesentralen.hypersys.GyldigToken

data class NestePersonAaRingeRequest(
        @JsonProperty("token") val token: GyldigToken,
        @JsonProperty("lokallagID") val lokallag: Lokallag
)