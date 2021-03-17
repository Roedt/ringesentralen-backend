package no.roedt.ringesentralen

import no.roedt.ringesentralen.samtale.resultat.Resultat

enum class Modus(val id: Int, vararg val gyldigeResultattyper: Resultat) {
    Velger(id=0,  Resultat.Ikke_svar, Resultat.Passet_ikke, Resultat.Samtale_startet, Resultat.Svarte),
    Medlem(id=1,  Resultat.Ikke_svar, Resultat.Passet_ikke, Resultat.Samtale_startet, Resultat.Svarte)
}
