package no.roedt.ringesentralen.samtale.resultat

import no.roedt.brukere.GroupID
import no.roedt.ringesentralen.brukere.RingesentralenGroupID

enum class Resultat(val nr: Int, val nesteGroupID: GroupID?) {
    Ikke_svar(0, null),
    Ringes_etter_valget(1, RingesentralenGroupID.Ferdigringt),
    Ikke_ringes_igjen(2, RingesentralenGroupID.Slett),
    Oppfoelging_toppkandidat(3, RingesentralenGroupID.TrengerOppfoelging),
    Passet_ikke(4, null),
    Vil_bli_valgkampfrivillig_og_aktiv(5, RingesentralenGroupID.Ferdigringt),
    Vil_bli_valgkampfrivillig(6, RingesentralenGroupID.Ferdigringt),
    Nei(7, null),
    Vil_bli_aktiv(8, RingesentralenGroupID.Ferdigringt),
    Samtale_startet(9, null),
    Flere_enn_to_ikke_svar(10, null),
    Svarte(11, RingesentralenGroupID.Ferdigringt),
    RingTilbake(12, RingesentralenGroupID.KlarTilAaRinges),
    UgyldigSvar(13, RingesentralenGroupID.KlarTilAaRinges)
}
