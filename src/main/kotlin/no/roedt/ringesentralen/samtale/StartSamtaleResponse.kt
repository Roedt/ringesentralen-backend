package no.roedt.ringesentralen.samtale

import java.time.LocalDateTime

data class StartSamtaleResponse(
        val ringerID: Long,
        val skalRingesID: Long,
        val timeStarted: LocalDateTime
)