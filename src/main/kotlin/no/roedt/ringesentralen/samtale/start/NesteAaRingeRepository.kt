package no.roedt.ringesentralen.samtale.start

import jakarta.enterprise.context.Dependent
import jakarta.persistence.EntityManager
import no.roedt.list
import no.roedt.ringesentralen.brukere.RingesentralenGroupID

@Dependent
class NesteAaRingeRepository(internal val entityManager: EntityManager) {
    fun hentNesteMedlem(
        fylke: Int,
        lokallag: Int,
        ringerLokallag: Int
    ) = entityManager.list(
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

    fun getTidlegareSamtalarMedDennePersonen(oppringtNummer: String) =
        entityManager.list(
            "SELECT resultat, ringerNavn, datetime, kommentar, ringtNavn, oppfoelgingId " +
                "FROM v_samtalerResultat " +
                "WHERE oppringtNummer = '$oppringtNummer'"
        )

    fun hentNesteIkkemedlem(
        fylke: Int,
        lokallag: Int
    ) = entityManager.list(
        """SELECT v.id FROM v_personerSomKanRinges v 
                WHERE fylke = $fylke 
                AND hypersysID is null 
                ORDER BY ABS(lokallag-'$lokallag') ASC, 
                hypersysID DESC,
                brukergruppe = ${RingesentralenGroupID.PrioritertAaRinge.nr} DESC,
                sisteSamtale ASC,
                v.hypersysID DESC
        """
    )
}
