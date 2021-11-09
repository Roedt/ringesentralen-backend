package no.roedt.ringesentralen.brukere

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Epost(
    val tittel: String,
    val tekst: String,
    val tekstAaLoggeHvisDeaktivert: String,
    val loggFoerSendingTekst: String
)
