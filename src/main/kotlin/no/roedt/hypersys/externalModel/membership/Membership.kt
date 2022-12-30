package no.roedt.hypersys.externalModel.membership

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Membership(
    @JsonProperty("member_id") val member_id: Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("first_name") val first_name: String,
    @JsonProperty("last_name") val last_name: String,
    @JsonProperty("birth") val birth: String?,
    @JsonProperty("email") val email: String?,
    @JsonProperty("gender") val gender: String,
    @JsonProperty("organisation") val organisation: String,
    @JsonProperty("organisation_shortname") val organisation_shortname: String,
    @JsonProperty("organisation_id") val organisation_id: Int,
    @JsonProperty("postal_address") val postal_address: PostalAddress?,

    @JsonProperty("mobile") val mobile: String,
    @JsonProperty("last_paid_year") val last_paid_year: String?,
    @JsonProperty("status") val status: String
)

class ListMembershipTypeReference : TypeReference<List<Membership>>()
