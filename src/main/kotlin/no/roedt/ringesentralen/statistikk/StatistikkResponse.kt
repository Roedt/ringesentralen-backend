package no.roedt.ringesentralen.statistikk

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class StatistikkResponse(
    val samtalerStatistikkResponse: SamtalerStatistikkResponse,
    val ringereStatistikkResponse: RingereStatistikkResponse
)
