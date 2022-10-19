package no.roedt.forum.underforum

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class TraadRequest(
    @JsonProperty("underforum") var underforum: String,
    @JsonProperty("node") var node: LinkedHashMap<String, Any>,
    @JsonProperty("id") var id: String
)