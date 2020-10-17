package no.roedt.ringesentralen

import no.roedt.ringesentralen.samtale.Resultat

enum class Modus(vararg val gyldigeResultattyper: Resultat) {
    Korona(Resultat.Ikke_svar, Resultat.Passet_ikke, Resultat.Samtale_startet, Resultat.Svarte),
}
