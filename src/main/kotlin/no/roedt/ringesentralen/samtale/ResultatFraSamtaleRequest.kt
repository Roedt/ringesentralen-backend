package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.Modus

@RegisterForReflection
data class ResultatFraSamtaleRequest(
        @JsonProperty("modus") val modus: Modus,
        @JsonProperty("ringtID") val ringtID: Long,
        @JsonProperty("result") val result: Resultat,
        @JsonProperty("kommentar") val kommentar: String,
        @JsonProperty("modusspesifikkeResultat") val modusspesifikkeResultat: ModusspesifikkeResultat,
        @JsonProperty("vilIkkeBliRingt") val vilIkkeBliRingt: Boolean
)