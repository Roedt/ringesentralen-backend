package no.roedt.ringesentralen.dashboard

import io.quarkus.panache.common.Sort
import no.roedt.ringesentralen.Lokallag
import no.roedt.ringesentralen.LokallagRepository
import no.roedt.ringesentralen.Ringer
import no.roedt.ringesentralen.samtale.GroupID
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

interface DashboardService {
    fun getDashboard(ringerID: Long): DashboardResponse
}

@ApplicationScoped
class DashboardServiceBean(
        val lokallagRepository: LokallagRepository,
        val entityManager: EntityManager
) : DashboardService {

    override fun getDashboard(ringerID: Long): DashboardResponse {
        val ringerinfo = entityManager.createNativeQuery("SELECT id, groupID, lokallag from person where id = $ringerID").resultList.first() as Array<*>
        val ringer = Ringer(id = (ringerinfo[0] as Int).toLong(), GroupID.from(ringerinfo[1] as Int), ringerinfo[2] as Int)
        val mineLokallag =
                if(ringer.isAdmin()) { lokallagRepository.findAll(Sort.ascending("name")).list() }
                else { lokallagRepository.find("id", ringer.lokallag).list<Lokallag>() }

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
}