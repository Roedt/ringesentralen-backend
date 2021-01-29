package no.roedt.ringesentralen.samtale

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class NestePersonAaRingeResponse(
    val ringbarPerson: RingbarPerson,
    val tidlegareSamtalar: List<Samtale>
)