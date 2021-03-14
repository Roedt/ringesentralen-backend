package no.roedt.ringesentralen.hypersys

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import org.eclipse.microprofile.jwt.JsonWebToken

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

        companion object {
                fun from(token: JsonWebToken) =
                        GyldigPersonToken(
                                access_token = token.claim<String>("hypersys.access_token").get(),
                                expires_in = token.claim<Any>("hypersys.expires_in").get().toString().toInt(),
                                token_type = token.claim<String>("hypersys.token_type").get(),
                                scope = token.claim<String>("hypersys.scope").get(),
                                refresh_token = token.claim<String>("hypersys.refresh_token").get(),
                                user_id = token.claim<String>("hypersys.user_id").get()
                        )
        }
}

@RegisterForReflection
data class UgyldigToken(
        @JsonProperty("error") val error: String
) : Token