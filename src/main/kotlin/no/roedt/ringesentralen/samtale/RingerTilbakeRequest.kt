package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty
import no.roedt.ringesentralen.hypersys.GyldigToken

data class RingerTilbakeRequest(
        @JsonProperty("token") val token: GyldigToken,
        @JsonProperty("ringerID") val ringerID: Long,
        @JsonProperty("ringtNummer") val ringtNummer: String
)
