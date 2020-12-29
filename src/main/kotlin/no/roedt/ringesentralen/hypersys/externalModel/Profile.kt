package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty

data class Profile(
        @JsonProperty("user") val user: User
)