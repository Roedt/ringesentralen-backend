package no.roedt.ringesentralen.hypersys

import com.fasterxml.jackson.annotation.JsonProperty

interface Token

data class GyldigToken(
        @JsonProperty("access_token") val access_token: String,
        @JsonProperty("expires_in") val expires_in: Int,
        @JsonProperty("token_type") val token_type: String,
        @JsonProperty("scope") val scope: String
) : Token

data class UgyldigToken(
        @JsonProperty("error") val error: String
) : Token
