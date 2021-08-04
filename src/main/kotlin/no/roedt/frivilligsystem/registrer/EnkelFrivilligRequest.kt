package no.roedt.frivilligsystem.registrer

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.person.UserId
import java.time.Instant

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
data class SMSFrivilligRequest(
    @JsonProperty("tidspunkt") var tidspunkt: Instant?,
    @JsonProperty("fornavn") var fornavn: String,
    @JsonProperty("etternavn") var etternavn: String,
    @JsonProperty("telefonnummer") var telefonnummer: String,
    @JsonProperty("postnummer") var postnummer: Int,
)

@RegisterForReflection
data class AutentisertSMSFrivilligRequest(
    val userId: UserId,
    val request: SMSFrivilligRequest
)
