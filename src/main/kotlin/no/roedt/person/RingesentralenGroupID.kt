package no.roedt.person

import no.roedt.brukere.GroupID
import no.roedt.brukere.GroupID.Companion.referencesOneOf
import no.roedt.ringesentralen.Roles

enum class RingesentralenGroupID(override val nr: Int, override val skildring: String, override val roller: Set<String>) :
    GroupID {
    ManglerSamtykke(0, "mangler info/samtykke før ringing", setOf()),
    KlarTilAaRinges(1, "klar til å ringes", setOf()),
    Ferdigringt(2, "ferdigringt", setOf()),
    Slett(3, "slett", setOf()),
    UgodkjentRinger(4, "ugodkjent ringer for velgere", setOf(Roles.venterPaaGodkjenning)),
    AvslaattRinger(5, "ringer som aktivt ikke er godkjent", setOf(Roles.sperret)),
    GodkjentRinger(6, "godkjent ringer", setOf(Roles.bruker, Roles.ringer)),
    TrengerOppfoelging(7, "trenger oppfølging", setOf()),
    LokalGodkjenner(8, "ringer som kan godkjenne ringere i sitt lokallag", setOf(Roles.bruker, Roles.ringer, Roles.ringerMedlemmer, Roles.godkjenner)),
    Admin(9, "admin", setOf(Roles.bruker, Roles.ringer, Roles.ringerMedlemmer, Roles.godkjenner, Roles.admin)),
    PrioritertAaRinge(10, "prioritert å ringe", setOf()),
    GodkjentRingerMedlemmer(11, "godkjent ringer for velgere og medlemmer", setOf(Roles.bruker, Roles.ringer, Roles.ringerMedlemmer)),
    Frivillig(12, "har meldt seg som valgkampfrivillig", setOf());

    companion object {
        fun from(value: Int): GroupID = values().first { it.nr == value }
        fun isBrukerEllerVenter(groupID: Int) = referencesOneOf(groupID, UgodkjentRinger, GodkjentRinger, GodkjentRingerMedlemmer, LokalGodkjenner, Admin)
        fun isIkkeRegistrertRinger(groupID: Int) = referencesOneOf(groupID, ManglerSamtykke, KlarTilAaRinges, Ferdigringt, Slett, PrioritertAaRinge, Frivillig)
    }
}
