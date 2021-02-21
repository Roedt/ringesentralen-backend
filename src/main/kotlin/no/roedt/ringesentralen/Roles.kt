package no.roedt.ringesentralen

class Roles {
    companion object Roles {
        const val ikke_godkjent = "ikke_godkjent"
        const val avslaatt = "avslaatt"
        const val ikke_registrert = "ikke_registrert"
        const val ringer = "ringer"
        const val godkjenner = "godkjenner"
        const val admin = "admin"
        const val ikkeGodkjentRingerGodkjennerAdmin = "$ikke_godkjent, $ringer, $godkjenner, $admin"
        const val ringerGodkjennerAdmin = "$ringer, $godkjenner, $admin"
        const val godkjennerAdmin = "$godkjenner, $admin"
    }
}