package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class RingerTilbakeRequest(
        @JsonProperty("ringerID") val ringerID: Long,
        @JsonProperty("ringtNummer") val ringtNummer: String
)
