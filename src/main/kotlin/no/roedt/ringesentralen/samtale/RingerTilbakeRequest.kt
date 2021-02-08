package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.UserId

@RegisterForReflection
data class RingerTilbakeRequest(
        @JsonProperty("ringtNummer") val ringtNummer: String
)

@RegisterForReflection
data class AutentisertRingerTilbakeRequest(val userId: UserId, val ringerTilbakeRequest: RingerTilbakeRequest) {
        fun ringtNummer() = ringerTilbakeRequest.ringtNummer
}