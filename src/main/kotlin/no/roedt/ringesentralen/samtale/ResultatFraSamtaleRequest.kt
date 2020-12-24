package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.hypersys.GyldigToken

data class ResultatFraSamtaleRequest(
        @JsonProperty("token") val token: GyldigToken,
        @JsonProperty("modus") val modus: Modus,
        @JsonProperty("ringerID") val ringerID: Long,
        @JsonProperty("ringtID") val ringtID: Long,
        @JsonProperty("resultat") val result: Resultat,
        @JsonProperty("kommentar") val kommentar: String,
        @JsonProperty("modusspesifikkeResultat") val modusspesifikkeResultat: ModusspesifikkeResultat,
        @JsonProperty("vilIkkeBliRingt") val vilIkkeBliRingt: Boolean
)