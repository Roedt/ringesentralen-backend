package no.roedt.ringesentralen.samtale

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.person.Person

@RegisterForReflection
data class NestePersonAaRingeResponse(
    val person: Person,
    val lokallagNavn: String,
    val tidlegareSamtalar: List<Samtale>
)