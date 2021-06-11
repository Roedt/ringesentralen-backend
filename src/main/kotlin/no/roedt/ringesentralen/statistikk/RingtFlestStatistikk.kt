package no.roedt.ringesentralen.statistikk

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class RingtFlestStatistikk (
    val jegHarRingt: Int,
    val maksRingt: Int
)
