package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Member(
        @JsonProperty("user_id") val user_id: Int,
        @JsonProperty("email") val email: String?,
        @JsonProperty("name") val name: String,
        @JsonProperty("phone") val phone: String,
        @JsonProperty("role") val role: String
)
