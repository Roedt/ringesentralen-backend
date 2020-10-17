package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty

data class KoronaspesifikkeResultat (
    @JsonProperty("vilHaKoronaprogram") val vilHaKoronaprogram: Boolean,
    @JsonProperty("vilBliMerAktiv") val vilBliMerAktiv: Boolean,
    @JsonProperty("vilHaValgkampsbrev") val vilHaValgkampsbrev: Boolean
) : ModusspesifikkeResultat