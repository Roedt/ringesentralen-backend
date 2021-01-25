package no.roedt.ringesentralen.hypersys

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class LoginRequest(
        @JsonProperty("key") val key: String,
        @JsonProperty("brukarnamn") val brukarnamn: String,
        @JsonProperty("passord") val passord: String
)