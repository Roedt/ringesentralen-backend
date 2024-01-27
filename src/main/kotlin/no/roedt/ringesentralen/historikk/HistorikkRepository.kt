package no.roedt.ringesentralen.historikk

import jakarta.enterprise.context.Dependent
import jakarta.persistence.EntityManager
import no.roedt.list
import no.roedt.person.UserId
import no.roedt.ringesentralen.Modus

@Dependent
class HistorikkRepository(internal val entityManager: EntityManager) {
    fun hentMineSamtaler(
        userId: UserId,
        modus: Modus
    ) = entityManager.list(
        "select resultat, ringerNavn, tidspunkt, kommentar, oppringtNummer, ringtNavn, oppfoelgingId " +
            "from v_mineSamtaler where hypersysID='${userId.userId}' and modus='${modus.name}'"
    )

    fun hentLagetsSamtaler(
        modus: Modus,
        lokallag: Int
    ) = entityManager.list(
        "select resultat, ringerNavn, tidspunkt, kommentar, oppringtNummer, ringtNavn, oppfoelgingId " +
            "from v_mineSamtaler where lokallag='$lokallag' and modus='${modus.name}'"
    )

    fun tellMineSamtaler(userId: UserId) =
        entityManager.list(
            "select count(1) from samtale s inner join ringer r on s.ringer=r.id " +
                "inner join person p on p.id=r.personId where hypersysID='${userId.userId}'"
        )
}
