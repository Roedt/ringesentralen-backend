package no.roedt.ringesentralen.samtale.start

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.person.UserId

@RegisterForReflection
data class RingerTilbakeRequest(
    @JsonProperty("ringtNummer") val ringtNummer: String
) {
    fun validate() = TelefonnummerValidator.validate(ringtNummer)
}

@RegisterForReflection
data class AutentisertRingerTilbakeRequest(
    val userId: UserId,
    val ringerTilbakeRequest: RingerTilbakeRequest,
    val modus: Modus,
    val groups: Set<String>
) {
    fun ringtNummer() = ringerTilbakeRequest.ringtNummer
    fun validate() = ringerTilbakeRequest.validate()
}
