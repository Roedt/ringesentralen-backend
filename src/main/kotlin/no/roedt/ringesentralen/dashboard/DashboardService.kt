package no.roedt.ringesentralen.dashboard

import UserId
import io.quarkus.panache.common.Sort
import no.roedt.ringesentralen.Lokallag
import no.roedt.ringesentralen.LokallagRepository
import no.roedt.ringesentralen.PersonRepository
import no.roedt.ringesentralen.samtale.GroupID
import no.roedt.ringesentralen.samtale.RingbarPerson
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

interface DashboardService {
    fun getDashboard(ringerID: UserId): DashboardResponse
}

@ApplicationScoped
class DashboardServiceBean(
        val lokallagRepository: LokallagRepository,
        val entityManager: EntityManager,
        val personRepository: PersonRepository
) : DashboardService {

    override fun getDashboard(ringerID: UserId): DashboardResponse {
        val mineLokallag = getMineLokallag(ringerID)

        val statusliste: List<Lokallagsstatus> = mineLokallag
                .map { lokallag ->
                    val igjenAaRinge = entityManager.createNativeQuery("SELECT 1 FROM v_igjenAaRinge WHERE lokallag = ${lokallag.id} ").resultList.size
                    val personerSomKanRinges = entityManager.createNativeQuery("SELECT 1 FROM v_personerSomKanRinges WHERE lokallag = ${lokallag.id}").resultList.size
                    val totaltInklRingte = entityManager.createNativeQuery("SELECT 1 FROM v_totaltInklRingte WHERE lokallag = ${lokallag.id}").resultList.size
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
            lokallagRepository.find("id", ringer.lokallag).list()
        }
    }

    private fun hypersysIdTilPerson(hypersysId: UserId) =
        personRepository.find("hypersysID", hypersysId.userId).firstResult<RingbarPerson>()
}