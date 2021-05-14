package no.roedt.ringesentralen.samtale.resultat

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Valg21SpesifikkeResultat(
    @JsonProperty("vilPolitikkLink") val vilPolitikkLink: Boolean,
    @JsonProperty("vilBliMerAktiv") val vilBliMerAktiv: Boolean,
    @JsonProperty("vilBliRingtAugust") val vilBliRingtAugust: Boolean,
    @JsonProperty("vilHaMedlemsLink") val vilHaMedlemsLink: Boolean,
    @JsonProperty("vilHaFellesskapLink") val vilHaFellesskapLink: Boolean
) : ModusspesifikkeResultat
