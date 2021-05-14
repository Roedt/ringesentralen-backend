package no.roedt.ringesentralen.samtale.resultat

import no.roedt.ringesentralen.person.GroupID

enum class Resultat(val nr: Int, val nesteGroupID: GroupID?) {
    Ikke_svar(0, null),
    Ringes_etter_valget(1, GroupID.Ferdigringt),
    Ikke_ringes_igjen(2, GroupID.Slett),
    Oppfoelging_toppkandidat(3, GroupID.TrengerOppfoelging),
    Passet_ikke(4, null),
    Vil_bli_valgkampfrivillig_og_aktiv(5, GroupID.Ferdigringt),
    Vil_bli_valgkampfrivillig(6, GroupID.Ferdigringt),
    Nei(7, null),
    Vil_bli_aktiv(8, GroupID.Ferdigringt),
    Samtale_startet(9, null),
    Flere_enn_to_ikke_svar(10, null),
    Svarte(11, GroupID.Ferdigringt),
    RingTilbake(12, GroupID.KlarTilAaRinges),
    UgyldigSvar(13, GroupID.KlarTilAaRinges)
}
