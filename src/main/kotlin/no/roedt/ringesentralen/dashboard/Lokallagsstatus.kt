package no.roedt.ringesentralen.dashboard

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.lokallag.Lokallag

@RegisterForReflection
data class Lokallagsstatus(
    val lokallag: Lokallag,
    val igjenAaRinge: Int,
    val personerSomKanRinges: Int,
    val totaltInklRingte: Int
)
