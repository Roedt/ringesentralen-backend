package no.roedt.ringesentralen.dashboard

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class DashboardResponse(
    val statusliste: List<Lokallagsstatus>
)
