package no.roedt.ringesentralen.dashboard

import io.quarkus.panache.common.Sort
import no.roedt.lokallag.Lokallag
import no.roedt.lokallag.LokallagRepository
import no.roedt.person.GroupID
import no.roedt.person.Person
import no.roedt.person.PersonRepository
import no.roedt.person.UserId
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Modus
import no.roedt.brukere.FylkeRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DashboardService(
    val lokallagRepository: LokallagRepository,
    val databaseUpdater: DatabaseUpdater,
    val personRepository: PersonRepository,
    val fylkeRepository: FylkeRepository
) {

    fun getDashboard(ringerID: UserId, modus: Modus): DashboardResponse {
        val mineLokallag = getMineLokallag(hypersysIdTilPerson(ringerID))
        val lokallagIDar = mineLokallag.map { it.id.toInt() }

        val hypersysID = if (modus == Modus.medlemmer) " is not null" else " is null"

        val igjenAaRingePerLokallag = databaseUpdater.getResultList("SELECT lokallag from person where groupID=${GroupID.KlarTilAaRinges.nr} and hypersysID $hypersysID").filter { lokallagIDar.contains(it) }.groupBy { it }
        val personerSomKanRingesPerLokallag = databaseUpdater.getResultList("SELECT lokallag FROM v_personerSomKanRinges where hypersysID $hypersysID").filter { lokallagIDar.contains(it) }.groupBy { it }
        val totaltInklRingtePerLokallag = databaseUpdater.getResultList("SELECT lokallag from person where groupID iN (${GroupID.Ferdigringt.nr}, ${GroupID.Slett.nr}) and hypersysID $hypersysID").filter { lokallagIDar.contains(it) }.groupBy { it }

        val statusliste: List<Lokallagsstatus> = mineLokallag
            .map { lokallag ->
                Lokallagsstatus(
                    lokallag = lokallag,
                    igjenAaRinge = (igjenAaRingePerLokallag.getOrDefault(lokallag.id.toInt(), listOf()) as List<*>).size,
                    personerSomKanRinges = (personerSomKanRingesPerLokallag.getOrDefault(lokallag.id.toInt(), listOf()) as List<*>).size,
                    totaltInklRingte = (totaltInklRingtePerLokallag.getOrDefault(lokallag.id.toInt(), listOf()) as List<*>).size,
                    fylke = fylkeRepository.findById(lokallag.fylke)
                )
            }
            .sortedBy { it.lokallag.navn }
            .toList()
        return DashboardResponse(statusliste = statusliste)
    }

    private fun getMineLokallag(ringer: Person): List<Lokallag> = when {
        GroupID.Admin.references(ringer.groupID()) -> lokallagRepository.findAll(Sort.ascending("navn")).list()
        GroupID.LokalGodkjenner.references(ringer.groupID()) -> lokallagRepository.fromFylke(ringer.fylke)
        else -> lokallagRepository.list("id", ringer.lokallag.toLong())
    }

    private fun hypersysIdTilPerson(hypersysId: UserId) = personRepository.find("hypersysID", hypersysId.userId).firstResult<Person>()
}
