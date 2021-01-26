package no.roedt.ringesentralen.samtale

import UserId
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class AutentisertNestePersonAaRingeRequest(val userId: UserId, val nestePersonAaRingeRequest: NestePersonAaRingeRequest) {
    fun lokallagId() = nestePersonAaRingeRequest.lokallag.id
}