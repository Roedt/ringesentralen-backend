package no.roedt.ringesentralen.brukere

import com.fasterxml.jackson.annotation.JsonProperty
import no.roedt.ringesentralen.hypersys.GyldigToken

data class HentBrukararRequest(
    @JsonProperty("token") val token: GyldigToken,
    @JsonProperty("utfoerende") val utfoerende: Long,
)