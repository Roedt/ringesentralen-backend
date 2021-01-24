package no.roedt.ringesentralen.hypersys

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class LoginRequest(
        @JsonProperty("key") var key: String = "",
        @JsonProperty("brukarnamn") var brukarnamn: String = "",
        @JsonProperty("passord") var passord: String = ""
)