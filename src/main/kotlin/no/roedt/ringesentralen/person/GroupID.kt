package no.roedt.ringesentralen.person

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
            when (groupID) {
                Admin.nr -> setOf("ringer", "admin", "godkjenner")
                LokalGodkjenner.nr -> setOf("ringer", "godkjenner")
                GodkjentRinger.nr -> setOf("ringer")
                else -> setOf("uatorisert")
            }
    }
}