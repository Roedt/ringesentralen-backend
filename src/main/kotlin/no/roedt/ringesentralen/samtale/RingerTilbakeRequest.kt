package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.person.UserId

@RegisterForReflection
data class RingerTilbakeRequest(
        @JsonProperty("ringtNummer") val ringtNummer: String
) {
        fun validate() = TelefonnummerValidator.validate(ringtNummer)
}

@RegisterForReflection
data class AutentisertRingerTilbakeRequest(val userId: UserId, val ringerTilbakeRequest: RingerTilbakeRequest) {
        fun ringtNummer() = ringerTilbakeRequest.ringtNummer
        fun validate() = ringerTilbakeRequest.validate()
}
