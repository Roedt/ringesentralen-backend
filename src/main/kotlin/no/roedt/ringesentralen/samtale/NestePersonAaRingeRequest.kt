package no.roedt.ringesentralen.samtale

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.Lokallag

@RegisterForReflection
data class NestePersonAaRingeRequest(
        @JsonProperty("lokallag") var lokallag: Lokallag
)