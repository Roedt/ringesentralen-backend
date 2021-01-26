package no.roedt.ringesentralen

import no.roedt.ringesentralen.samtale.Resultat

enum class Modus(val id: Int, vararg val gyldigeResultattyper: Resultat) {
    Korona(id = 2, Resultat.Ikke_svar, Resultat.Passet_ikke, Resultat.Samtale_startet, Resultat.Svarte),
}
