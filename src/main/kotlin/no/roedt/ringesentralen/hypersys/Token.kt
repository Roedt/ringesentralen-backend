package no.roedt.ringesentralen.hypersys

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

interface Token

@RegisterForReflection
data class GyldigToken(
        @JsonProperty("access_token") val access_token: String,
        @JsonProperty("expires_in") val expires_in: Int,
        @JsonProperty("token_type") val token_type: String,
        @JsonProperty("scope") val scope: String
) : Token

@RegisterForReflection
data class UgyldigToken(
        @JsonProperty("error") val error: String
) : Token
