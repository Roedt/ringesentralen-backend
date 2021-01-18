package no.roedt.ringesentralen.hypersys

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginRequest(
        @JsonProperty("key") val key: String,
        @JsonProperty("brukarnamn") val brukarnamn: String,
        @JsonProperty("passord") val passord: String
)