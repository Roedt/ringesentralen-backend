package no.roedt.ringesentralen.samtale.resultat

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class KoronaspesifikkeResultat (
    @JsonProperty("vilHaKoronaprogram") val vilHaKoronaprogram: Boolean,
    @JsonProperty("vilBliMerAktiv") val vilBliMerAktiv: Boolean,
    @JsonProperty("vilHaValgkampsbrev") val vilHaValgkampsbrev: Boolean
) : ModusspesifikkeResultat