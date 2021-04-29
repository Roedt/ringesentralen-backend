package no.roedt.frivilligsystem

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.frivilligsystem.registrer.AktivitetForFrivillig

@RegisterForReflection
data class FrivilligResponse(
    val frivillig: Frivillig,
    val aktiviteter: List<AktivitetForFrivillig>
)