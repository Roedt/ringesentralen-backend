package no.roedt.ringesentralen.dashboard

import io.quarkus.panache.common.Sort
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.lokallag.Lokallag
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import javax.enterprise.context.ApplicationScoped

interface DashboardService {
    fun getDashboard(ringerID: UserId, modus: Modus): DashboardResponse
}

@ApplicationScoped
class DashboardServiceBean(
    val lokallagRepository: LokallagRepository,
    val databaseUpdater: DatabaseUpdater,
    val personRepository: PersonRepository
) : DashboardService {

    override fun getDashboard(ringerID: UserId, modus: Modus): DashboardResponse {
        val mineLokallag = getMineLokallag(ringerID)
        val lokallagIDar = mineLokallag.map { it.id.toInt() }

        val hypersysID = if (modus == Modus.medlemmer) " is not null" else " is null"

        val igjenAaRingePerLokallag = databaseUpdater.getResultList("SELECT lokallag from person where groupID=${GroupID.KlarTilAaRinges.nr} and hypersysID $hypersysID").filter { lokallagIDar.contains(it) }.groupBy { it }
        val personerSomKanRingesPerLokallag = databaseUpdater.getResultList("SELECT lokallag FROM v_personerSomKanRinges where hypersysID $hypersysID").filter { lokallagIDar.contains(it) }.groupBy { it }
        val totaltInklRingtePerLokallag = databaseUpdater.getResultList("SELECT lokallag from person where groupID=${GroupID.Ferdigringt.nr} and hypersysID $hypersysID").filter { lokallagIDar.contains(it) }.groupBy { it }

        val statusliste: List<Lokallagsstatus> = mineLokallag
            .map { lokallag ->
                Lokallagsstatus(
                    lokallag = lokallag,
                    igjenAaRinge = (igjenAaRingePerLokallag.getOrDefault(lokallag.id.toInt(), listOf()) as List<*>).size,
                    personerSomKanRinges = (personerSomKanRingesPerLokallag.getOrDefault(lokallag.id.toInt(), listOf()) as List<*>).size,
                    totaltInklRingte = (totaltInklRingtePerLokallag.getOrDefault(lokallag.id.toInt(), listOf()) as List<*>).size
                )
            }
            .toList()
        return DashboardResponse(statusliste = statusliste)
    }

    private fun getMineLokallag(ringerID: UserId): List<Lokallag> {
        val ringer = hypersysIdTilPerson(ringerID)
        return if (GroupID.Admin.references(ringer.groupID)) {
            lokallagRepository.findAll(Sort.ascending("navn")).list()
        } else {
            lokallagRepository.list("id", ringer.lokallag.toLong())
        }
    }

    private fun hypersysIdTilPerson(hypersysId: UserId) =
        personRepository.find("hypersysID", hypersysId.userId).firstResult<Person>()
}