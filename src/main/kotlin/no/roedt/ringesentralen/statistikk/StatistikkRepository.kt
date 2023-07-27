package no.roedt.ringesentralen.statistikk

import jakarta.enterprise.context.Dependent
import jakarta.persistence.EntityManager
import no.roedt.list
import no.roedt.ringesentralen.samtale.resultat.Resultat

@Dependent
class StatistikkRepository(internal val entityManager: EntityManager) {
    fun getSamtalerStatistikkResponse(): SamtalerStatistikkResponse {
        val list = entityManager.list("SELECT id, displaytext FROM `resultat` ")
            .map { it as Array<*> }
            .map { Resultattype(id = it[0] as Int, displaytext = it[1].toString()) }
            .map {
                SamtaleResultat(
                    displaytext = it.displaytext,
                    antal = entityManager.list("SELECT ringer FROM `samtale` WHERE resultat = ${it.id}").size
                )
            }
            .filter { it.antal > 0 }

        return SamtalerStatistikkResponse(
            resultat = list,
            samtalerMedResultatSaaLangt = entityManager.list("SELECT ringer FROM `samtale` WHERE resultat!=${Resultat.Samtale_startet.nr}").size
        )
    }
}