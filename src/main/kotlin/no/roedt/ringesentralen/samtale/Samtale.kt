package no.roedt.ringesentralen.samtale

import io.quarkus.runtime.annotations.RegisterForReflection
import java.time.LocalDateTime

@RegisterForReflection
data class Samtale(
    val tidspunkt: LocalDateTime,
    val ringer: String,
    val kommentar: String,
    val resultat: String,
    val ringtNummer: String,
    val ringtNavn: String
)
