package no.roedt.forum.underforum

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class TraadMedInnhold(
    val traad: Traad,
    val innhold: Map<String, Any>?
)