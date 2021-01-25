package no.roedt.ringesentralen

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.samtale.GroupID

@RegisterForReflection
data class Ringer(
        val id: Long,
        val groupID: GroupID,
        val lokallag: Lokallag
) {
    fun isAdmin(): Boolean {
        return groupID == GroupID.Admin
    }
}