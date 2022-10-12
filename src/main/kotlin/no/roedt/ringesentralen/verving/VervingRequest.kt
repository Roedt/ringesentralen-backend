package no.roedt.ringesentralen.verving

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class VervingRequest(
    @JsonProperty("telefonnummer") val telefonnummer: String,
    @JsonProperty("fornavn") val fornavn: String,
    @JsonProperty("etternavn") val etternavn: String,
    @JsonProperty("postnummer") val postnummer: Int,
    @JsonProperty("verversNavn") val verversNavn: String?
)

@RegisterForReflection
data class AutentisertVervingRequest(val request: VervingRequest)
