package no.roedt.ringesentralen.samtale

import UserId
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class AutentisertRingerTilbakeRequest(val userId: UserId, val ringerTilbakeRequest: RingerTilbakeRequest) {
    fun ringtNummer() = ringerTilbakeRequest.ringtNummer
}