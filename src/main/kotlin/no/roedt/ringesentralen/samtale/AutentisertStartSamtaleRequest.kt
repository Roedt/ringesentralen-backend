package no.roedt.ringesentralen.samtale

import UserId
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class AutentisertStartSamtaleRequest(val userId: UserId, val startSamtaleRequest: StartSamtaleRequest) {
    fun skalRingesID() = startSamtaleRequest.skalRingesID
}