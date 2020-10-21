package no.roedt.ringesentralen

import no.roedt.ringesentralen.samtale.GroupID

data class Ringer(
        val id: Long,
        val groupID: GroupID,
        val lokallag: Lokallag
) {
    fun isAdmin(): Boolean {
        return groupID == GroupID.Admin
    }
}