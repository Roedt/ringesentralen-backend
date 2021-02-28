package no.roedt.ringesentralen.dashboard

import io.quarkus.panache.common.Sort
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.lokallag.Lokallag
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import javax.enterprise.context.ApplicationScoped

interface DashboardService {
    fun getDashboard(ringerID: UserId): DashboardResponse
}

@ApplicationScoped
class DashboardServiceBean(
    val lokallagRepository: LokallagRepository,
    val databaseUpdater: DatabaseUpdater,
    val personRepository: PersonRepository
) : DashboardService {

    override fun getDashboard(ringerID: UserId): DashboardResponse {
        val mineLokallag = getMineLokallag(ringerID)

        val statusliste: List<Lokallagsstatus> = mineLokallag
                .map { lokallag ->
                    val igjenAaRinge = databaseUpdater.count("SELECT 1 FROM v_igjenAaRinge WHERE lokallag = ${lokallag.id} ")
                    val personerSomKanRinges = databaseUpdater.count("SELECT 1 FROM v_personerSomKanRinges WHERE lokallag = ${lokallag.id}")
                    val totaltInklRingte = databaseUpdater.count("SELECT 1 FROM v_totaltInklRingte WHERE lokallag = ${lokallag.id}")
                    Lokallagsstatus(
                            lokallag = lokallag,
                            igjenAaRinge = igjenAaRinge,
                            personerSomKanRinges = personerSomKanRinges,
                            totaltInklRingte = totaltInklRingte + igjenAaRinge
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
            lokallagRepository.find("id", ringer.lokallag.toLong()).list()
        }
    }

    private fun hypersysIdTilPerson(hypersysId: UserId) =
        personRepository.find("hypersysID", hypersysId.userId).firstResult<Person>()
}