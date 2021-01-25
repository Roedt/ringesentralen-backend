package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class SingleOrgan (
        @JsonProperty("id") val id: Int,
        @JsonProperty("name") val name: String,
        @JsonProperty("organ_type") val organ_type: Int,
        @JsonProperty("organisation") val organisation: Int,
        @JsonProperty("members") val members: List<Member>?
)