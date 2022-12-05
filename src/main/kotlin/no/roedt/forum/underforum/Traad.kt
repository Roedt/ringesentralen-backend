package no.roedt.forum.underforum

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Traad(
    val tittel: String,
    val underforum: String
)
