package no.roedt.ringesentralen.brukere

import com.fasterxml.jackson.annotation.JsonProperty
import no.roedt.ringesentralen.hypersys.GyldigToken

data class TilgangsendringsRequest(
        @JsonProperty("token") val token: GyldigToken,
        @JsonProperty("utfoerende") val utfoerende: Long,
        @JsonProperty("personMedEndraTilgang") val personMedEndraTilgang: Long
)