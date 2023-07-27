package no.roedt.ringesentralen.dashboard

import jakarta.enterprise.context.Dependent
import jakarta.persistence.EntityManager
import no.roedt.list
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.brukere.RingesentralenGroupID

@Dependent
class DashboardRepository(internal val entityManager: EntityManager) {
    fun hentIgjenAaRingePerLokallag(lokallagIDar: List<Int>, modus: Modus): Map<Any?, List<Any?>> {
        val hypersysID = if (modus == Modus.medlemmer) " is not null" else " is null"
        return entityManager.list("SELECT lokallag from person where groupID=${RingesentralenGroupID.KlarTilAaRinges.nr} and hypersysID $hypersysID")
            .filter { lokallagIDar.contains(it) }.groupBy { it }
    }

    fun hentPersonerSomKanRingesPerLokallag(lokallagIDar: List<Int>, modus: Modus): Map<Any?, List<Any?>> {
        val hypersysID = if (modus == Modus.medlemmer) " is not null" else " is null"
        return entityManager.list("SELECT lokallag FROM v_personerSomKanRinges where hypersysID $hypersysID")
            .filter { lokallagIDar.contains(it) }.groupBy { it }
    }

    fun hentTotaltInklRingtePerLokallag(lokallagIDar: List<Int>, modus: Modus): Map<Any?, List<Any?>> {
        val hypersysID = if (modus == Modus.medlemmer) " is not null" else " is null"
        return entityManager.list("SELECT lokallag from person where groupID iN (${RingesentralenGroupID.Ferdigringt.nr}, ${RingesentralenGroupID.Slett.nr}) and hypersysID $hypersysID")
            .filter { lokallagIDar.contains(it) }.groupBy { it }
    }
}
