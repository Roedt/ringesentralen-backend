package no.roedt.ringesentralen.brukere

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.person.UserId

@RegisterForReflection
data class AutentisertGetBrukereRequest(
    val userId: UserId,
    val groups: Set<String>
)
