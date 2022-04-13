package no.roedt.brukere

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.person.UserId

@RegisterForReflection
data class AutentisertGetBrukereRequest(
    val userId: UserId,
    val groups: Set<String>
)
