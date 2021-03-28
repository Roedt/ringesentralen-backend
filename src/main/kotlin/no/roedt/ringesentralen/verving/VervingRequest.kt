package no.roedt.ringesentralen.verving

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.person.UserId

@RegisterForReflection
data class VervingRequest(
    @JsonProperty("telefonnummer") val telefonnummer: String,
    @JsonProperty("fornavn") val fornavn: String,
    @JsonProperty("etternavn") val etternavn: String,
    @JsonProperty("postnummer") val postnummer: Int
)

@RegisterForReflection
data class AutentisertVervingRequest(val request: VervingRequest, val userId: UserId)