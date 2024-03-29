package no.roedt.ringesentralen.brukere

import no.roedt.brukere.GenerellRolle
import no.roedt.brukere.GroupID
import no.roedt.brukere.GroupID.Companion.referencesOneOf
import no.roedt.forum.ForumRolle
import no.roedt.ringesentralen.RingespesifikkRolle

enum class RingesentralenGroupID(override val nr: Int, override val skildring: String, override val roller: Set<String>) :
    GroupID {
    ManglerSamtykke(0, "mangler info/samtykke før ringing", setOf()),
    KlarTilAaRinges(1, "klar til å ringes", setOf()),
    Ferdigringt(2, "ferdigringt", setOf()),
    Slett(3, "slett", setOf()),
    UgodkjentRinger(4, "ugodkjent ringer for velgere", setOf(GenerellRolle.VENTER_PAA_GODKJENNING, ForumRolle.DEBATTANT)),
    AvslaattRinger(5, "ringer som aktivt ikke er godkjent", setOf(GenerellRolle.SPERRET)),
    GodkjentRinger(6, "godkjent ringer", setOf(GenerellRolle.BRUKER, RingespesifikkRolle.RINGER, ForumRolle.DEBATTANT)),
    TrengerOppfoelging(7, "trenger oppfølging", setOf()),
    LokalGodkjenner(
        8,
        "ringer som kan godkjenne ringere i sitt lokallag",
        setOf(
            GenerellRolle.BRUKER,
            RingespesifikkRolle.RINGER,
            RingespesifikkRolle.RINGER_MEDLEMMER,
            RingespesifikkRolle.GODKJENNER,
            ForumRolle.DEBATTANT
        )
    ),
    Admin(
        9,
        "admin",
        setOf(
            GenerellRolle.BRUKER,
            RingespesifikkRolle.RINGER,
            RingespesifikkRolle.RINGER_MEDLEMMER,
            RingespesifikkRolle.GODKJENNER,
            GenerellRolle.ADMIN,
            ForumRolle.DEBATTANT
        )
    ),
    PrioritertAaRinge(10, "prioritert å ringe", setOf()),
    GodkjentRingerMedlemmer(
        11,
        "godkjent ringer for velgere og medlemmer",
        setOf(GenerellRolle.BRUKER, RingespesifikkRolle.RINGER, RingespesifikkRolle.RINGER_MEDLEMMER, ForumRolle.DEBATTANT)
    ),
    Frivillig(12, "har meldt seg som valgkampfrivillig", setOf());

    companion object {
        fun from(value: Int): GroupID = entries.first { it.nr == value }

        fun erUgyldigRolleForAaBrukeSystemet(groupID: GroupID) =
            setOf(
                ManglerSamtykke,
                KlarTilAaRinges,
                Ferdigringt,
                Slett
            ).contains(groupID)

        fun isBrukerEllerVenter(groupID: Int) =
            referencesOneOf(
                groupID,
                UgodkjentRinger,
                GodkjentRinger,
                GodkjentRingerMedlemmer,
                LokalGodkjenner,
                Admin
            )

        fun isIkkeRegistrertRinger(groupID: Int) =
            referencesOneOf(
                groupID,
                ManglerSamtykke,
                KlarTilAaRinges,
                Ferdigringt,
                Slett,
                PrioritertAaRinge,
                Frivillig
            )
    }
}
