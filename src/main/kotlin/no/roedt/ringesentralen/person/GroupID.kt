package no.roedt.ringesentralen.person

import no.roedt.ringesentralen.Roles

enum class GroupID(val nr: Int, val skildring: String, val roller: Set<String>) {
    ManglerSamtykke(0, "mangler info/samtykke før ringing", setOf()),
    KlarTilAaRinges(1, "klar til å ringes", setOf()),
    Ferdigringt(2, "ferdigringt", setOf()),
    Slett(3, "slett", setOf()),
    UgodkjentRinger(4, "ugodkjent ringer", setOf(Roles.venterPaaGodkjenning)),
    AvslaattRinger(5, "ringer som aktivt ikke er godkjent", setOf(Roles.sperret)),
    GodkjentRinger(6, "godkjent ringer", setOf(Roles.bruker, Roles.ringer)),
    TrengerOppfoelging(7, "trenger oppfølging", setOf()),
    LokalGodkjenner(8, "ringer som kan godkjenne ringere i sitt lokallag", setOf(Roles.bruker, Roles.ringer, Roles.godkjenner)),
    Admin(9, "admin", setOf(Roles.bruker, Roles.ringer, Roles.godkjenner, Roles.admin));

    fun references(value: Int): Boolean = nr == value

    companion object {
        fun from(value: Int): GroupID = values().first { it.nr == value }
        fun referencesOneOf(groupID: Int, vararg groupIDs: GroupID) = groupIDs.map { it.nr }.any { it == groupID }
    }
}