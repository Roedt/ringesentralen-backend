package no.roedt.ringesentralen.brukere

import com.fasterxml.jackson.annotation.JsonProperty

data class HentBrukararRequest(
    @JsonProperty("utfoerende") val utfoerende: Long,
)