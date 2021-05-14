package no.roedt.ringesentralen.statistikk

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class SamtalerStatistikkResponse(
    val resultat: List<SamtaleResultat>,
    val samtalerMedResultatSaaLangt: Int
)
