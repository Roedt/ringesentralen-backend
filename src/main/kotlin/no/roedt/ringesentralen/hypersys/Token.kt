package no.roedt.ringesentralen.hypersys

import com.fasterxml.jackson.annotation.JsonProperty

data class Token(
        @JsonProperty("access_token") val access_token: String,
        @JsonProperty("expires_in") val expires_in: Int,
        @JsonProperty("token_type") val token_type: String,
        @JsonProperty("scope") val scope: String
)
