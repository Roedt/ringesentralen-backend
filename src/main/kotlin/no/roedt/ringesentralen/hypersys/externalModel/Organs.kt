package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Organs(
        @JsonProperty("organs") val organs: List<Organ>
)
