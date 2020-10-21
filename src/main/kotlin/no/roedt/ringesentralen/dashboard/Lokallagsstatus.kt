package no.roedt.ringesentralen.dashboard

import no.roedt.ringesentralen.Lokallag

data class Lokallagsstatus(
        val lokallag: Lokallag,
        val igjenAaRinge: Int,
        val personerSomKanRinges: Int,
        val totaltInklRingte: Int
)
