package no.roedt.ringesentralen

import no.roedt.brukere.GenerellRolle.GenerellRolle.ADMIN

class RingespesifikkRolle {
    companion object RingespesifikkRolle {
        const val RINGER = "ringer"
        const val RINGER_MEDLEMMER = "ringerForMedlemmer"
        const val GODKJENNER = "godkjenner"
        const val GODKJENNER_ADMIN = "$GODKJENNER, $ADMIN"
        const val RINGER_FOR_MEDLEMMER_GODKJENNER_ADMIN = "$RINGER_MEDLEMMER, $GODKJENNER, $ADMIN"
        const val RINGER_GODKJENNER_ADMIN = "$RINGER, $RINGER_MEDLEMMER, $GODKJENNER, $ADMIN"
    }
}
