package no.roedt.ringesentralen

import no.roedt.brukere.GenerellRolle.GenerelleRoller.admin

class RingespesifikkRolle {
    companion object {
        const val ringer = "ringer"
        const val ringerMedlemmer = "ringerForMedlemmer"
        const val godkjenner = "godkjenner"
        const val godkjennerAdmin = "$godkjenner, $admin"
        const val ringerForMedlemmerGodkjennerAdmin = "$ringerMedlemmer, $godkjenner, $admin"
        const val ringerGodkjennerAdmin = "$ringer, $ringerMedlemmer, $godkjenner, $admin"
    }
}
