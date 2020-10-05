package no.roedt.ringesentralen.samtale

import java.time.LocalDateTime

data class StartSamtaleResponse(var ringerID: Long, var skalRingesID: Long, var timeStarted: LocalDateTime) {
    constructor() : this(-1, -1, LocalDateTime.MIN)
}