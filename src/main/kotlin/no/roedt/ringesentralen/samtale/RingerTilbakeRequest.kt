package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty

data class RingerTilbakeRequest(
        @JsonProperty("ringerID") val ringerID: Long,
        @JsonProperty("ringtNummer") val ringtNummer: String
)
