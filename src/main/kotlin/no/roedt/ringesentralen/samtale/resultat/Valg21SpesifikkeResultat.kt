package no.roedt.ringesentralen.samtale.resultat

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Valg21SpesifikkeResultat (
    @JsonProperty("vilHaKoronaprogram") val vilHaKoronaprogram: Boolean,
    @JsonProperty("vilBliMerAktiv") val vilBliMerAktiv: Boolean,
    @JsonProperty("vilHaValgkampsbrev") val vilHaValgkampsbrev: Boolean,
    @JsonProperty("vilHaMedlemsLink") val vilHaMedlemsLink: Boolean,
    @JsonProperty("vilHaFellesskapLink") val vilHaFellesskapLink: Boolean,
    @JsonProperty("vilHaNyhetsbrevLink") val vilHaNyhetsbrevLink: Boolean,
) : ModusspesifikkeResultat