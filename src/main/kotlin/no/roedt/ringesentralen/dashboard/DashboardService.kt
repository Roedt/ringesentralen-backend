package no.roedt.ringesentralen.dashboard

import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import no.roedt.fylke.FylkeService
import no.roedt.lokallag.Lokallag
import no.roedt.lokallag.LokallagService
import no.roedt.person.Person
import no.roedt.person.PersonService
import no.roedt.person.UserId
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.brukere.RingesentralenGroupID

@ApplicationScoped
class DashboardService(
    val lokallagService: LokallagService,
    val personService: PersonService,
    val fylkeService: FylkeService,
    val repository: DashboardRepository
) {
    fun getDashboard(
        ringerID: UserId,
        modus: Modus
    ): DashboardResponse {
        val mineLokallag = getMineLokallag(hypersysIdTilPerson(ringerID))
        val lokallagIDar = mineLokallag.map { it.id.toInt() }

        val igjenAaRingePerLokallag = repository.hentIgjenAaRingePerLokallag(lokallagIDar, modus)
        val personerSomKanRingesPerLokallag = repository.hentPersonerSomKanRingesPerLokallag(lokallagIDar, modus)
        val totaltInklRingtePerLokallag = repository.hentTotaltInklRingtePerLokallag(lokallagIDar, modus)

        val statusliste: List<Lokallagsstatus> =
            mineLokallag
                .map { lokallag ->
                    Lokallagsstatus(
                        lokallag = lokallag,
                        igjenAaRinge =
                            (
                                igjenAaRingePerLokallag.getOrDefault(
                                    lokallag.id.toInt(),
                                    listOf()
                                ) as List<*>
                            ).size,
                        personerSomKanRinges =
                            (
                                personerSomKanRingesPerLokallag.getOrDefault(
                                    lokallag.id.toInt(),
                                    listOf()
                                ) as List<*>
                            ).size,
                        totaltInklRingte =
                            (
                                totaltInklRingtePerLokallag.getOrDefault(
                                    lokallag.id.toInt(),
                                    listOf()
                                ) as List<*>
                            ).size,
                        fylke = fylkeService.findById(lokallag.fylke)
                    )
                }
                .sortedBy { it.lokallag.navn }
                .toList()
        return DashboardResponse(statusliste = statusliste)
    }

    private fun getMineLokallag(ringer: Person): List<Lokallag> =
        when {
            RingesentralenGroupID.Admin.references(ringer.groupID()) -> lokallagService.findAll(Sort.ascending("navn"))

            RingesentralenGroupID.LokalGodkjenner.references(ringer.groupID()) -> lokallagService.fromFylke(ringer.fylke)
            else -> lokallagService.list(ringer.lokallag)
        }

    private fun hypersysIdTilPerson(hypersysId: UserId) = personService.finnFraHypersysId(hypersysId.userId).firstResult<Person>()
}
