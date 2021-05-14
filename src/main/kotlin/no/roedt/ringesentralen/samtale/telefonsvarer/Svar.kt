package no.roedt.ringesentralen.samtale.telefonsvarer

import no.roedt.ringesentralen.samtale.resultat.Resultat

enum class Svar(val resultat: Resultat) {
    ringTilbake(Resultat.RingTilbake),
    ikkeRingIgjen(Resultat.Ikke_ringes_igjen),
    ugyldigSvar(Resultat.RingTilbake),
    svarteIkke(Resultat.Ikke_svar)
}
