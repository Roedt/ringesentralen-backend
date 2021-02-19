package no.roedt.ringesentralen.samtale.resultat

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.person.UserId

@RegisterForReflection
data class ResultatFraSamtaleRequest(
        @JsonProperty("modus") val modus: Modus,
        @JsonProperty("ringtID") val ringtID: Long,
        @JsonProperty("resultat") val resultat: Resultat,
        @JsonProperty("kommentar") val kommentar: String,
        @JsonProperty("modusspesifikkeResultat") val modusspesifikkeResultat: ModusspesifikkeResultat,
        @JsonProperty("vilIkkeBliRingt") val vilIkkeBliRingt: Boolean
) {
        fun isGyldigResultat(): Boolean = resultat in modus.gyldigeResultattyper

        fun skalRegistrere() = modus == Modus.Korona && resultat == Resultat.Svarte

        fun validate(): Boolean {
                return true
        }
}

@RegisterForReflection
data class AutentisertResultatFraSamtaleRequest(val userId: UserId, val request: ResultatFraSamtaleRequest) {
        fun validate() = request.validate()
}