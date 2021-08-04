package no.roedt.ringesentralen.sms

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.person.UserId

@RegisterForReflection
data class OppdaterSMSRequest (
    val smsId: Long,
    val status: Utsendingsstatus,
    val mottakere: List<Long>
)

@RegisterForReflection
data class AutentisertOppdaterSMSRequest(
    val userId: UserId,
    val request: OppdaterSMSRequest
)