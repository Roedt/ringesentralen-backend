package no.roedt.ringesentralen.statistikk

import no.roedt.ringesentralen.Modus
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

@ApplicationScoped
class StatistikkService(val entityManager: EntityManager) {

    fun getStatistikk(): StatistikkResponse {
        val modus = Modus.Korona
        return StatistikkResponse(
            samtalerStatistikkResponse = getSamtalerStatistikkResponse(modus)
        )
    }

    private fun getSamtalerStatistikkResponse(modus: Modus) : SamtalerStatistikkResponse {
        val list = get("SELECT id, displaytext FROM v_resultatForModus where modus=${modus.id} ORDER BY id ASC")
            .map { it as Array<Any> }
            .map { Resultattype(id = it[0] as Int, displaytext = it[1].toString()) }
            .map {
                SamtaleResultat(
                    displaytext = it.displaytext,
                    antal = get("SELECT callerPhone FROM `call` WHERE result = ${it.id} AND typeCall = 1").size
                )
            }

        return SamtalerStatistikkResponse(
            resultat = list,
            samtalerMedResultatSaaLangt = get("SELECT callerPhone FROM `call` WHERE typeCall = 1 and result != 9").size
        )
    }

    private fun get(query: String) = entityManager.createNativeQuery(query).resultList
}