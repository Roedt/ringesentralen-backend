package no.roedt.ringesentralen.hypersys.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Organisasjonsledd(
        @JsonProperty("id") val id: Int,
        @JsonProperty("name") val name: String,
        @JsonProperty("organisation_number") val organisation_number: String?,
        @JsonProperty("type_name") val type_name: String?,
        @JsonProperty("parent") val parent: Int?,
        @JsonProperty("parent_name") val parent_name: String?,
        @JsonProperty("children") val children: Array<Int>?,
        @JsonProperty("status") val status: Int
)
