package no.roedt.ringesentralen.person

import no.roedt.ringesentralen.Roles

enum class GroupID(val nr: Int, val skildring: String, val roller: Set<String>) {
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
    GodkjentRingerMedlemmer(11, "godkjent ringer for velgere og medlemmer", setOf(Roles.bruker, Roles.ringer, Roles.ringerMedlemmer));

    fun references(value: Int): Boolean = nr == value

    fun oneOf(vararg value: Int) : Boolean = value.contains(nr)

    companion object {
        fun from(value: Int): GroupID = values().first { it.nr == value }
        fun referencesOneOf(groupID: Int, vararg groupIDs: GroupID) = groupIDs.map { it.nr }.any { it == groupID }
        fun isBrukerEllerVenter(groupID: Int) = referencesOneOf(groupID, UgodkjentRinger, GodkjentRinger, GodkjentRingerMedlemmer, LokalGodkjenner, Admin)
        fun isIkkeRegistrertRinger(groupID: Int) = referencesOneOf(groupID, ManglerSamtykke, KlarTilAaRinges, Ferdigringt, Slett, PrioritertAaRinge)
    }
}