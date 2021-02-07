package no.roedt.ringesentralen.historikk

import UserId
import no.roedt.ringesentralen.samtale.Samtale
import java.sql.Timestamp
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

@ApplicationScoped
class HistorikkService(private val entityManager: EntityManager) {

    fun getMineSataler(userId: UserId): List<Samtale> {
        val sql =
            "select " +
                    "resultat, " +
                    "ringerNavn, " +
                    "tidspunkt, " +
                    "kommentar, " +
                    "oppringtNummer, " +
                    "ringtNavn, " +
                    "merAktiv, " +
                    "valgkampsbrev " +
                    "from v_mineSamtaler " +
                    "where hypersysID='${userId.userId}'"
        return entityManager.createNativeQuery(sql)
            .resultList
            .map { it as Array<*> }
            .map {
                Samtale(
                    resultat = it[0] as String,
                    ringer = it[1] as String,
                    tidspunkt = (it[2] as Timestamp).toLocalDateTime(),
                    kommentar = it[3] as String,
                    ringtNummer = it[4] as String,
                    ringtNavn = it[5] as String
                )
            }
    }
}