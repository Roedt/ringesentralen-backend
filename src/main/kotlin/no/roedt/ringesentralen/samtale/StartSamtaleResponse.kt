package no.roedt.ringesentralen.samtale

import io.quarkus.runtime.annotations.RegisterForReflection
import java.time.LocalDateTime

@RegisterForReflection
data class StartSamtaleResponse(
        val skalRingesID: Long,
        val timeStarted: LocalDateTime
)