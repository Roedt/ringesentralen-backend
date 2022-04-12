package no.roedt.ringesentralen.sms

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.person.UserId

@RegisterForReflection
data class LagreSMSRequest(
    val tekst: String,
    val mottakere: List<Int>
)

@RegisterForReflection
data class AutentisertLagreSMSRequest(
    val userId: UserId,
    val request: LagreSMSRequest
)
