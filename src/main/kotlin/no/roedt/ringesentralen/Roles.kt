package no.roedt.ringesentralen

class Roles {
    companion object Roles {
        const val uautorisert = "uautorisert"
        const val ringer = "ringer"
        const val godkjenner = "godkjenner"
        const val admin = "admin"
        const val uautorisertRingerGodkjennerAdmin = "$uautorisert, $ringer, $godkjenner, $admin"
        const val ringerGodkjennerAdmin = "$ringer, $godkjenner, $admin"
        const val godkjennerAdmin = "$godkjenner, $admin"
    }
}