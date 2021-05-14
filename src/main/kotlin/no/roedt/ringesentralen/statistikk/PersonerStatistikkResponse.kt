package no.roedt.ringesentralen.statistikk

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class PersonerStatistikkResponse(
    val antallPersonerISystemetTotalt: Int,
    val ringere: Int,
    val ferdigringte: Int,
    val ringtUtenSvar: Int,
    val ikkeRingt: Int,
    val antallLokallagMedPersonerTilknytta: Int
)
