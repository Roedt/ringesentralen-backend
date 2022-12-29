package no.roedt.frivilligsystem.registrer

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.person.UserId

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
data class SoMeFrivilligRequest(
    @JsonProperty("fornavn") var fornavn: String,
    @JsonProperty("etternavn") var etternavn: String,
    @JsonProperty("telefonnummer") var telefonnummer: String,
    @JsonProperty("postnummer") var postnummer: String,
    @JsonProperty("email") var email: String?
)

@RegisterForReflection
data class AutentisertSoMeFrivilligRequest(
    val userId: UserId,
    val request: SoMeFrivilligRequest
)
