package no.roedt.frivilligsystem.kontakt

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.person.Person
import java.util.Date

@RegisterForReflection
data class KontaktResponse(
    val frivillig_id: Int,
    val tilbakemelding: String,
    val registrert_av: Person,
    val datetime: Date
)