package no.roedt.ringesentralen.samtale

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Samtale(
    val tidspunkt: String,
    val ringer: String,
    val kommentar: String,
    val resultat: String,
    val ringtNummer: String,
    val ringtNavn: String
)
