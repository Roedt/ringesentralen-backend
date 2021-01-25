package no.roedt.ringesentralen.samtale

import io.quarkus.runtime.annotations.RegisterForReflection
import java.time.LocalDateTime

@RegisterForReflection
data class StartSamtaleResponse(
        val ringerID: Long,
        val skalRingesID: Long,
        val timeStarted: LocalDateTime
)