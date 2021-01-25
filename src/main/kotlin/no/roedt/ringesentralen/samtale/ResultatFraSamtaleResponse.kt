package no.roedt.ringesentralen.samtale

import io.quarkus.runtime.annotations.RegisterForReflection
import java.time.LocalDateTime

@RegisterForReflection
data class ResultatFraSamtaleResponse(val oppdatert: LocalDateTime)