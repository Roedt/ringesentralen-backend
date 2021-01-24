package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.Modus

@RegisterForReflection
data class ResultatFraSamtaleRequest(
        @JsonProperty("modus") var modus: Modus,
        @JsonProperty("ringerID") var ringerID: Long,
        @JsonProperty("ringtID") var ringtID: Long,
        @JsonProperty("resultat") var result: Resultat,
        @JsonProperty("kommentar") var kommentar: String,
        @JsonProperty("modusspesifikkeResultat") var modusspesifikkeResultat: ModusspesifikkeResultat,
        @JsonProperty("vilIkkeBliRingt") var vilIkkeBliRingt: Boolean
)