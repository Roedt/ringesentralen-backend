package no.roedt.brukere

interface GroupID {
    val nr: Int
    val name: String
    val skildring: String
    val roller: Set<String>

    fun references(value: Int): Boolean = nr == value

    companion object {
        fun referencesOneOf(
            groupID: Int,
            vararg groupIDs: GroupID
        ) = groupIDs.map { it.nr }.any { it == groupID }
    }
}
