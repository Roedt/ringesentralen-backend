package no.roedt.ringesentralen.samtale

data class StartSamtaleRequest(var ringerID: Long, var skalRingesID: Long) {
    constructor() : this(-1, -1)
}