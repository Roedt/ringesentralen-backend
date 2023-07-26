package no.roedt.hypersys.konvertering

import jakarta.enterprise.context.Dependent
import no.roedt.hypersys.externalModel.Membership
import no.roedt.lokallag.LokallagService

@Dependent
class LokallagConverter(private val lokallagService: LokallagService) {

    fun tilLokallag(memberships: List<Membership>): Int =
        getOrganisationName(memberships)?.let { lokallagService.fromOrganisationName(it) } ?: -1

    fun tilLokallag(organisasjon: String) = lokallagService.fromOrganisationName(organisasjon)

    private fun getOrganisationName(memberships: List<Membership>) =
        memberships
            .asSequence()
            .sortedByDescending { it.startDate }
            .map { it.organisationName }
            .firstOrNull()
}
