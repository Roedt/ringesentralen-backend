package no.roedt.ringesentralen.samtale.resultat

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.UserId

@RegisterForReflection
data class ResultatFraSamtaleRequest(
        @JsonProperty("modus") val modus: Modus,
        @JsonProperty("ringtID") val ringtID: Long,
        @JsonProperty("resultat") val resultat: Resultat,
        @JsonProperty("kommentar") val kommentar: String,
        @JsonProperty("modusspesifikkeResultat") val modusspesifikkeResultat: ModusspesifikkeResultat,
        @JsonProperty("vilIkkeBliRingt") val vilIkkeBliRingt: Boolean
)

@RegisterForReflection
data class AutentisertResultatFraSamtaleRequest(val userId: UserId, val resultatFraSamtaleRequest: ResultatFraSamtaleRequest)