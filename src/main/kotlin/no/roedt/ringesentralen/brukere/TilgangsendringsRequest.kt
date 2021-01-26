package no.roedt.ringesentralen.brukere

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class TilgangsendringsRequest(
        @JsonProperty("personMedEndraTilgang") val personMedEndraTilgang: Long
)