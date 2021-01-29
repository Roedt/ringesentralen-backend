package no.roedt.ringesentralen.statistikk

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class RingereStatistikkResponse(
    val registrerteRingere: Int,
    val antallSomHarRingt: Int,
    val aktiveRingereDenSisteTimen: Int,
    val aktiveRingereIDag: Int,
    val lokaleGodkjennere: Int,
    val avvisteRingere: Int,
    val antallLokallagRingtFraTotalt: Int
)
