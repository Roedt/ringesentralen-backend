package no.roedt.frivilligsystem

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.frivilligsystem.registrer.AktivitetForFrivillig
import no.roedt.ringesentralen.person.Person

@RegisterForReflection
data class FrivilligResponse(
    val frivillig: Frivillig,
    val person: Person,
    val aktiviteter: List<AktivitetForFrivillig>
)