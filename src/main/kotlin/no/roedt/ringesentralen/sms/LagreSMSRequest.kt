package no.roedt.ringesentralen.sms

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.person.UserId

@RegisterForReflection
data class LagreSMSRequest(
    val tekst: String,
    val mottakere: List<Long>
)

@RegisterForReflection
data class AutentisertLagreSMSRequest(
    val userId: UserId,
    val request: LagreSMSRequest
)
