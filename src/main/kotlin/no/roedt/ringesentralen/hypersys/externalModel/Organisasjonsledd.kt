package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Organisasjonsledd(
        @JsonProperty("id") val id: Int,
        @JsonProperty("name") val name: String,
        @JsonProperty("organisation_number") val organisation_number: String?,
        @JsonProperty("type_name") val type_name: String?,
        @JsonProperty("parent") val parent: Int?,
        @JsonProperty("parent_name") val parent_name: String?,
        @JsonProperty("short_name") val short_name: String?,
        @JsonProperty("description") val description: String?,
        @JsonProperty("children") val children: Array<Int>?,
        @JsonProperty("status") val status: Int
)
