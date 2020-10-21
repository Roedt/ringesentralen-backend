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

    private fun getMineLokallag(ringerID: Long): List<Lokallag> {
        val ringer = toRinger(ringerID)
        return if (ringer.isAdmin()) {
            lokallagRepository.findAll(Sort.ascending("name")).list()
        } else {
            lokallagRepository.find("id", ringer.lokallag).list()
        }
    }

    private fun toRinger(ringerID: Long): Ringer {
        val ringerinfo = entityManager.createNativeQuery("SELECT id, groupID, lokallag from person where id = $ringerID").resultList.first() as Array<*>
        return Ringer(
                id = ringerinfo[0].toLong(),
                groupID = GroupID.from(ringerinfo[1] as Int),
                lokallag = lokallagRepository.findById(ringerinfo[2].toLong())
        )
    }

    fun Any?.toLong() : Long = (this as Int).toLong()
}