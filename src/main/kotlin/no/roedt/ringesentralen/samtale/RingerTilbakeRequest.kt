package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class RingerTilbakeRequest(
        @JsonProperty("ringtNummer") val ringtNummer: String
)
