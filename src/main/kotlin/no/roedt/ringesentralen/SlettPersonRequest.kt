package no.roedt.ringesentralen

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class SlettPersonRequest(@JsonProperty("telefonnummer") val telefonnummer: String)