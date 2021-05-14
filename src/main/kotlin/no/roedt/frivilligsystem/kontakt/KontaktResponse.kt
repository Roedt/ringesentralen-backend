package no.roedt.frivilligsystem.kontakt

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.person.Person
import java.time.Instant

@RegisterForReflection
data class KontaktResponse(
    val frivillig_id: Int,
    val tilbakemelding: String,
    val registrert_av: Person,
    val datetime: Instant
)
