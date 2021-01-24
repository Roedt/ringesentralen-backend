package no.roedt.ringesentralen.brukere

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class TilgangsendringsRequest(
        @JsonProperty("utfoerende") var utfoerende: Long,
        @JsonProperty("personMedEndraTilgang") var personMedEndraTilgang: Long
)