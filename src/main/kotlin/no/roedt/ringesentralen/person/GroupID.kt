package no.roedt.ringesentralen.person

import no.roedt.ringesentralen.Roles

enum class GroupID(val nr: Int, val skildring: String) {
    ManglerSmtykke(0, "mangler info/samtykke før ringing"),
    KlarTilAaRinges(1, "klar til å ringes"),
    Ferdigringt(2, "ferdigringt"),
    Slett(3, "slett"),
    UgodkjentRinger(4, "ugodkjent ringer"),
    AvslaattRinger(5, "ringer som aktivt ikke er godkjent"),
    GodkjentRinger(6, "godkjent ringer og relay-bruker"),
    TrengerOppfoelging(7, "trenger oppfølging"),
    LokalGodkjenner(8, "ringer som kan godkjenne ringere i sitt lokallag"),
    Admin(9, "admin");

    fun references(value: Int): Boolean = nr == value

    companion object {
        fun from(value: Int): GroupID = values().first { it.nr == value }

        fun getRoller(groupID: Int) =
            when (from(groupID)) {
                Admin -> setOf(Roles.bruker, Roles.ringer, Roles.godkjenner, Roles.admin)
                LokalGodkjenner -> setOf(Roles.bruker, Roles.ringer, Roles.godkjenner)
                GodkjentRinger -> setOf(Roles.bruker, Roles.ringer)
                UgodkjentRinger -> setOf(Roles.venterPaaGodkjenning)
                AvslaattRinger -> setOf(Roles.sperret)
                else -> setOf()
            }
    }
}