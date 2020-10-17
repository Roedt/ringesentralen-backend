package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty

data class ResultatFraSamtaleRequest(
        @JsonProperty("ringerID") val ringerID: Long,
        @JsonProperty("ringtID") val ringtID: Long,
        @JsonProperty("result") val result: Int, //TODO: Bruk heller enum her?
        @JsonProperty("comment") val comment: String,
        @JsonProperty("vilHaKoronaprogram") val vilHaKoronaprogram: Boolean,
        @JsonProperty("vilBliMerAktiv") val vilBliMerAktiv: Boolean,
        @JsonProperty("vilHaValgkampsbrev") val vilHaValgkampsbrev: Boolean,
        @JsonProperty("vilIkkeBliRingt") val vilIkkeBliRingt: Boolean
)