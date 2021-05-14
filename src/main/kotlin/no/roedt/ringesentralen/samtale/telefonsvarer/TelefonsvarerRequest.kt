package no.roedt.ringesentralen.samtale.telefonsvarer

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.person.UserId
import no.roedt.ringesentralen.samtale.resultat.Resultat

@RegisterForReflection
data class TelefonsvarerRequest(
    @JsonProperty("telefonnummer") val telefonnummer: String,
    @JsonProperty("svar") val svar: Svar
)

@RegisterForReflection
data class AutentisertTelefonsvarerRequest(
    val userId: UserId,
    val request: TelefonsvarerRequest
) {
    fun resultat(): Resultat = request.svar.resultat
}
