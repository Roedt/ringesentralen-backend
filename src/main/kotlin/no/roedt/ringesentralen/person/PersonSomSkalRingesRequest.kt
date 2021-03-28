package no.roedt.ringesentralen.person

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class PersonSomSkalRingesRequest(
    @JsonProperty("telefonnummer") val telefonnummer: String,
    @JsonProperty("fornavn") val fornavn: String,
    @JsonProperty("etternavn") val etternavn: String,
    @JsonProperty("postnummer") val postnummer: Int
)

@RegisterForReflection
data class AutentisertPersonSomSkalRingesRequest(val request: PersonSomSkalRingesRequest, val userId: UserId )