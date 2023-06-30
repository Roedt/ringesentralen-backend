package no.roedt.ringesentralen.sms

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.person.UserId

@RegisterForReflection
data class SendSMSRequest(val fra: String, val til: String, val melding: String)

@RegisterForReflection
data class AutentisertSendSMSRequest(
    val userId: UserId,
    val request: SendSMSRequest
)
