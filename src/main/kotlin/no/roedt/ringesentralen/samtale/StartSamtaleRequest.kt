package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty
import no.roedt.ringesentralen.hypersys.GyldigToken

data class StartSamtaleRequest(
        @JsonProperty("token") val token: GyldigToken,
        @JsonProperty("ringerID") val ringerID: Long,
        @JsonProperty("skalRingesID") val skalRingesID: Long
)