package no.roedt.ringesentralen.hypersys

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

interface Token

interface GyldigToken : Token {
        fun access_token(): String
}


@RegisterForReflection
data class GyldigSystemToken(
        @JsonProperty("access_token") val access_token: String,
        @JsonProperty("expires_in") val expires_in: Int,
        @JsonProperty("token_type") val token_type: String,
        @JsonProperty("scope") val scope: String
) : GyldigToken {
        override fun access_token() = access_token
}

@RegisterForReflection
data class GyldigPersonToken(
        @JsonProperty("access_token") val access_token: String,
        @JsonProperty("expires_in") val expires_in: Int,
        @JsonProperty("token_type") val token_type: String,
        @JsonProperty("scope") val scope: String,
        @JsonProperty("refresh_token") val refresh_token: String,
        @JsonProperty("user_id") val user_id: String
) : GyldigToken {
        override fun access_token() = access_token
}

@RegisterForReflection
data class UgyldigToken(
        @JsonProperty("error") val error: String
) : Token