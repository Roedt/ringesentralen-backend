package no.roedt.ringesentralen.dashboard

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.lokallag.Lokallag
import no.roedt.brukere.Fylke

@RegisterForReflection
data class Lokallagsstatus(
    val lokallag: Lokallag,
    val igjenAaRinge: Int,
    val personerSomKanRinges: Int,
    val totaltInklRingte: Int,
    val fylke: Fylke
)
