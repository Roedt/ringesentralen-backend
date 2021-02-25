package no.roedt.ringesentralen

class Roles {
    companion object Roles {
        const val sperret = "sperret"
        const val venterPaaGodkjenning = "venter_paa_godkjenning"
        const val bruker = "bruker"
        const val ringer = "ringer"
        const val godkjenner = "godkjenner"
        const val admin = "admin"
        const val godkjennerAdmin = "$godkjenner, $admin"
    }
}