package no.roedt.brukere

class GenerellRolle {
    companion object GenerellRolle {
        const val SYSTEMBRUKER_FRONTEND = "systembrukerFrontend"
        const val SPERRET = "sperret"
        const val VENTER_PAA_GODKJENNING = "venter_paa_godkjenning"
        const val BRUKER = "bruker"
        const val ADMIN = "admin"
        const val BRUKER_VENTER_PAA_GODKJENNING = "$BRUKER, $VENTER_PAA_GODKJENNING"
    }
}
