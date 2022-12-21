package no.roedt.brukere.mfa

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class MFARequest(
    val enhetsid: String,
    val brukernavn: String
)

@RegisterForReflection
data class DekryptertMFARequest(
    val enhetsid: String,
    val brukernavn: String
)