package no.roedt.ringesentralen

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Telefonnummer(
        val landkode: String = "+47",
        val nummer: Int
)