package no.roedt.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class IsMember(
    @JsonProperty("id") val user_id: Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("paid") val paid: Boolean,
    @JsonProperty("is_member") val is_member: Boolean
)
