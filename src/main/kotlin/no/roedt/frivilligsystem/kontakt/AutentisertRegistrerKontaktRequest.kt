package no.roedt.frivilligsystem.kontakt

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.person.UserId

@RegisterForReflection
data class AutentisertRegistrerKontaktRequest(
    val userId: UserId,
    val request: RegistrerKontaktRequest
)
