package no.roedt.ringesentralen.statistikk

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class StatistikkResponse(
    val samtalerStatistikkResponse: SamtalerStatistikkResponse,
    val ringereStatistikkResponse: RingereStatistikkResponse?,
    val personerStatistikkResponse: PersonerStatistikkResponse?,
    val ringteIValkampen2023: List<Pair<String, Int>> = listOf()
)
