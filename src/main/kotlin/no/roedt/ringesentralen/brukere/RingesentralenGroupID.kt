package no.roedt.ringesentralen.brukere

import no.roedt.brukere.GenerelleRoller
import no.roedt.brukere.GroupID
import no.roedt.brukere.GroupID.Companion.referencesOneOf
import no.roedt.ringesentralen.Roles

enum class RingesentralenGroupID(override val nr: Int, override val skildring: String, override val roller: Set<String>) :
    GroupID {
    ManglerSamtykke(0, "mangler info/samtykke før ringing", setOf()),
    KlarTilAaRinges(1, "klar til å ringes", setOf()),
    Ferdigringt(2, "ferdigringt", setOf()),
    Slett(3, "slett", setOf()),
    UgodkjentRinger(4, "ugodkjent ringer for velgere", setOf(GenerelleRoller.venterPaaGodkjenning)),
    AvslaattRinger(5, "ringer som aktivt ikke er godkjent", setOf(GenerelleRoller.sperret)),
    GodkjentRinger(6, "godkjent ringer", setOf(GenerelleRoller.bruker, Roles.ringer)),
    TrengerOppfoelging(7, "trenger oppfølging", setOf()),
    LokalGodkjenner(8, "ringer som kan godkjenne ringere i sitt lokallag", setOf(GenerelleRoller.bruker, Roles.ringer, Roles.ringerMedlemmer, Roles.godkjenner)),
    Admin(9, "admin", setOf(GenerelleRoller.bruker, Roles.ringer, Roles.ringerMedlemmer, Roles.godkjenner, GenerelleRoller.admin)),
    PrioritertAaRinge(10, "prioritert å ringe", setOf()),
    GodkjentRingerMedlemmer(11, "godkjent ringer for velgere og medlemmer", setOf(GenerelleRoller.bruker, Roles.ringer, Roles.ringerMedlemmer)),
    Frivillig(12, "har meldt seg som valgkampfrivillig", setOf());

    companion object {
        fun from(value: Int): GroupID = values().first { it.nr == value }
        fun isBrukerEllerVenter(groupID: Int) = referencesOneOf(groupID, UgodkjentRinger, GodkjentRinger, GodkjentRingerMedlemmer, LokalGodkjenner, Admin)
        fun isIkkeRegistrertRinger(groupID: Int) = referencesOneOf(groupID, ManglerSamtykke, KlarTilAaRinges, Ferdigringt, Slett, PrioritertAaRinge, Frivillig)
        fun maks(nyBrukergruppe: Int, eksisterendeBrukergruppe: Int): GroupID =
            when {
                AvslaattRinger.oneOf(nyBrukergruppe, eksisterendeBrukergruppe) -> AvslaattRinger
                Admin.oneOf(nyBrukergruppe, eksisterendeBrukergruppe) -> Admin
                LokalGodkjenner.oneOf(nyBrukergruppe, eksisterendeBrukergruppe) -> LokalGodkjenner
                GodkjentRingerMedlemmer.oneOf(nyBrukergruppe, eksisterendeBrukergruppe) -> GodkjentRingerMedlemmer
                GodkjentRinger.oneOf(nyBrukergruppe, eksisterendeBrukergruppe) -> GodkjentRinger
                UgodkjentRinger.oneOf(nyBrukergruppe, eksisterendeBrukergruppe) -> UgodkjentRinger
                Frivillig.oneOf(nyBrukergruppe, eksisterendeBrukergruppe) -> Frivillig
                PrioritertAaRinge.oneOf(nyBrukergruppe, eksisterendeBrukergruppe) -> PrioritertAaRinge
                TrengerOppfoelging.oneOf(nyBrukergruppe, eksisterendeBrukergruppe) -> TrengerOppfoelging
                Slett.oneOf(nyBrukergruppe, eksisterendeBrukergruppe) -> Slett
                Ferdigringt.oneOf(nyBrukergruppe, eksisterendeBrukergruppe) -> Ferdigringt
                KlarTilAaRinges.oneOf(nyBrukergruppe, eksisterendeBrukergruppe) -> KlarTilAaRinges
                ManglerSamtykke.oneOf(nyBrukergruppe, eksisterendeBrukergruppe) -> ManglerSamtykke
                else -> ManglerSamtykke
            }
    }
}
