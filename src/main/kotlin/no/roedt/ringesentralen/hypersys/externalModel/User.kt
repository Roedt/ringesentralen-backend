package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class User(
        @JsonProperty("id") val id: Int,
        @JsonProperty("name") val name: String,
        @JsonProperty("email") val email: String,
        @JsonProperty("phone") val phone: String,
        @JsonProperty("phone2") val phone2: String,
        @JsonProperty("roles") val roles: List<Any>,
        @JsonProperty("memberships") val memberships: List<Membership>,
        @JsonProperty("addresses") val addresses: List<Address>
)