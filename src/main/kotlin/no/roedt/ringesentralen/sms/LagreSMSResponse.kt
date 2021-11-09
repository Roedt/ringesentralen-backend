package no.roedt.ringesentralen.sms

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class LagreSMSResponse(
    val id: Long
)
