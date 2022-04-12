package no.roedt.ringesentralen.samtale.start

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.person.UserId
import no.roedt.ringesentralen.Modus

@RegisterForReflection
data class AutentisertNestePersonAaRingeRequest(
    val userId: UserId,
    val modus: Modus,
    val lokallag: Int,
    val roller: Set<String>
) {
    fun userId() = userId.userId
}
