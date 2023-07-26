package no.roedt.ringesentralen.dashboard

import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import no.roedt.DatabaseUpdater
import no.roedt.brukere.FylkeRepository
import no.roedt.lokallag.Lokallag
import no.roedt.lokallag.LokallagService
import no.roedt.person.Person
import no.roedt.person.PersonRepository
import no.roedt.person.UserId
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.brukere.RingesentralenGroupID

@ApplicationScoped
class DashboardService(
    val lokallagService: LokallagService,
    val databaseUpdater: DatabaseUpdater,
    val personRepository: PersonRepository,
    val fylkeRepository: FylkeRepository
) {

    fun getDashboard(ringerID: UserId, modus: Modus): DashboardResponse {
        val mineLokallag = getMineLokallag(hypersysIdTilPerson(ringerID))
        val lokallagIDar = mineLokallag.map { it.id.toInt() }

        val hypersysID = if (modus == Modus.medlemmer) " is not null" else " is null"

        val igjenAaRingePerLokallag =
            databaseUpdater.getResultList("SELECT lokallag from person where groupID=${RingesentralenGroupID.KlarTilAaRinges.nr} and hypersysID $hypersysID")
                .filter { lokallagIDar.contains(it) }.groupBy { it }
        val personerSomKanRingesPerLokallag =
            databaseUpdater.getResultList("SELECT lokallag FROM v_personerSomKanRinges where hypersysID $hypersysID")
                .filter { lokallagIDar.contains(it) }.groupBy { it }
        val totaltInklRingtePerLokallag =
            databaseUpdater.getResultList("SELECT lokallag from person where groupID iN (${RingesentralenGroupID.Ferdigringt.nr}, ${RingesentralenGroupID.Slett.nr}) and hypersysID $hypersysID")
                .filter { lokallagIDar.contains(it) }.groupBy { it }

        val statusliste: List<Lokallagsstatus> = mineLokallag
            .map { lokallag ->
                Lokallagsstatus(
                    lokallag = lokallag,
                    igjenAaRinge = (
                        igjenAaRingePerLokallag.getOrDefault(
                            lokallag.id.toInt(),
                            listOf()
                        ) as List<*>
                        ).size,
                    personerSomKanRinges = (
                        personerSomKanRingesPerLokallag.getOrDefault(
                            lokallag.id.toInt(),
                            listOf()
                        ) as List<*>
                        ).size,
                    totaltInklRingte = (
                        totaltInklRingtePerLokallag.getOrDefault(
                            lokallag.id.toInt(),
                            listOf()
                        ) as List<*>
                        ).size,
                    fylke = fylkeRepository.findById(lokallag.fylke)
                )
            }
            .sortedBy { it.lokallag.navn }
            .toList()
        return DashboardResponse(statusliste = statusliste)
    }

    private fun getMineLokallag(ringer: Person): List<Lokallag> = when {
        RingesentralenGroupID.Admin.references(ringer.groupID()) -> lokallagService.findAll(Sort.ascending("navn"))

        RingesentralenGroupID.LokalGodkjenner.references(ringer.groupID()) -> lokallagService.fromFylke(ringer.fylke)
        else -> lokallagService.list(ringer.lokallag)
    }

    private fun hypersysIdTilPerson(hypersysId: UserId) =
        personRepository.find("hypersysID", hypersysId.userId).firstResult<Person>()
}
