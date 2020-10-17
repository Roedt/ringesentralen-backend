package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty
import no.roedt.ringesentralen.Modus

data class ResultatFraSamtaleRequest(
        @JsonProperty("modus") val modus: Modus,
        @JsonProperty("ringerID") val ringerID: Long,
        @JsonProperty("ringtID") val ringtID: Long,
        @JsonProperty("resultat") val result: Resultat,
        @JsonProperty("kommentar") val kommentar: String,
        @JsonProperty("modusspesifikkeResultat") val modusspesifikkeResultat: ModusspesifikkeResultat,
        @JsonProperty("vilIkkeBliRingt") val vilIkkeBliRingt: Boolean
)