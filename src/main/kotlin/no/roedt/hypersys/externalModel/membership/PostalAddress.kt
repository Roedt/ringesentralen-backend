package no.roedt.hypersys.externalModel.membership

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class PostalAddress(
    @JsonProperty("address1") val address1: String?,
    @JsonProperty("address2") val address2: String?,
    @JsonProperty("postal_code") val postal_code: String?,
    @JsonProperty("postal_code_name") val postal_code_name: String?,
    @JsonProperty("municipality") val municipality: Any?,
    @JsonProperty("county") val county: String?,
    @JsonProperty("country") val country: String?
)
