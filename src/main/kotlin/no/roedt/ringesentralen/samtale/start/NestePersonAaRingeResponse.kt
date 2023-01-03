package no.roedt.ringesentralen.samtale.start

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.person.PersonDTO
import no.roedt.ringesentralen.samtale.Samtale

@RegisterForReflection
data class NestePersonAaRingeResponse(
    val person: PersonDTO,
    val lokallagNavn: String,
    val tidlegareSamtalar: List<Samtale>
)
