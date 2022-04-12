package no.roedt.ringesentralen.verving

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.person.UserId

@RegisterForReflection
data class MottaSvarRequest(
    @JsonProperty("telefonnummer") val telefonnummer: String,
    @JsonProperty("svar") val svar: Boolean
)

@RegisterForReflection
data class AutentisertMottaSvarRequest(
    val request: MottaSvarRequest,
    val userId: UserId
)
