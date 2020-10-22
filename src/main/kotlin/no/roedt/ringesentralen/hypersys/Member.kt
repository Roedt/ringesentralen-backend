package no.roedt.ringesentralen.hypersys

import com.fasterxml.jackson.annotation.JsonProperty

data class Member(
        @JsonProperty("user_id") val user_id: Int,
        @JsonProperty("email") val email: String?,
        @JsonProperty("name") val name: String,
        @JsonProperty("phone") val phone: String,
        @JsonProperty("role") val role: String
)
