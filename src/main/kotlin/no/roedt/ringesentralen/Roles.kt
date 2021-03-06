package no.roedt.ringesentralen

class Roles {
    companion object Roles {
        const val systembrukerFrontend = "systembrukerFrontend"
        const val sperret = "sperret"
        const val venterPaaGodkjenning = "venter_paa_godkjenning"
        const val bruker = "bruker"
        const val ringer = "ringer"
        const val ringerMedlemmer = "ringerForMedlemmer"
        const val godkjenner = "godkjenner"
        const val admin = "admin"
        const val godkjennerAdmin = "$godkjenner, $admin"
        const val ringerForMedlemmerGodkjennerAdmin = "$ringerMedlemmer, $godkjenner, $admin"
        const val ringerGodkjennerAdmin = "$ringer, $ringerMedlemmer, $godkjenner, $admin"
        const val brukerVenterPaaGodkjenning = "$bruker, $venterPaaGodkjenning"
    }
}
