package no.roedt.ringesentralen.hypersys.externalModel.membership

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Membership(
    @JsonProperty("member_id") val member_id: Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("first_name") val first_name: String,
    @JsonProperty("last_name") val last_name: String,
    @JsonProperty("birth") val birth: String,
    @JsonProperty("email") val email: String?,
    @JsonProperty("gender") val gender: String,
    @JsonProperty("organisation") val organisation: String,
    @JsonProperty("organisation_shortname") val organisation_shortname: String,
    @JsonProperty("organisation_id") val organisation_id: Int,
    @JsonProperty("postal_address") val postal_address: String,

    @JsonProperty("phone") val phone: String,
    @JsonProperty("role") val role: String

)