package no.roedt.brukere

class GenerellRolle {
    companion object GenerellRolle {
        const val systembrukerFrontend = "systembrukerFrontend"
        const val sperret = "sperret"
        const val venterPaaGodkjenning = "venter_paa_godkjenning"
        const val bruker = "bruker"
        const val admin = "admin"
        const val brukerVenterPaaGodkjenning = "$bruker, $venterPaaGodkjenning"
    }
}
