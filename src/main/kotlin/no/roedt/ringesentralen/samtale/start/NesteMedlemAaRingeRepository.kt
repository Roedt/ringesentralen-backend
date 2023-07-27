package no.roedt.ringesentralen.samtale.start

import jakarta.enterprise.context.Dependent
import jakarta.persistence.EntityManager
import no.roedt.list

@Dependent
class NesteMedlemAaRingeRepository(internal val entityManager: EntityManager) {
    fun hentNestePerson(fylke: Int, lokallag: Int, ringerLokallag: Int) =
        entityManager.list(
            """SELECT v.id FROM v_personerSomKanRinges v
                WHERE fylke = $fylke 
                AND lokallag=$lokallag AND hypersysID is not null 
                ORDER BY ABS(lokallag-'$ringerLokallag') ASC,
                hypersysID DESC,
                sisteSamtale ASC,
                v.hypersysID DESC
        """
        )
            .map { it as Int }
            .firstOrNull()
}
