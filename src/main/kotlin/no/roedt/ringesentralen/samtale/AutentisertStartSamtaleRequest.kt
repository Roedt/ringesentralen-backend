package no.roedt.ringesentralen.samtale

import no.roedt.ringesentralen.UserId
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class AutentisertStartSamtaleRequest(val userId: UserId, val startSamtaleRequest: StartSamtaleRequest) {
    fun skalRingesID() = startSamtaleRequest.skalRingesID
}