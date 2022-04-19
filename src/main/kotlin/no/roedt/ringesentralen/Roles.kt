package no.roedt.ringesentralen

import no.roedt.brukere.GenerelleRoller.GenerelleRoller.admin

class Roles {
    companion object Roles {
        const val ringer = "ringer"
        const val ringerMedlemmer = "ringerForMedlemmer"
        const val godkjenner = "godkjenner"
        const val godkjennerAdmin = "$godkjenner, $admin"
        const val ringerForMedlemmerGodkjennerAdmin = "$ringerMedlemmer, $godkjenner, $admin"
        const val ringerGodkjennerAdmin = "$ringer, $ringerMedlemmer, $godkjenner, $admin"
    }
}
