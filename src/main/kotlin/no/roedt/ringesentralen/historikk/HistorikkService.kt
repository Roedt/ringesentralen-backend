package no.roedt.ringesentralen.historikk

import no.roedt.ringesentralen.UserId
import no.roedt.ringesentralen.PersonRepository
import no.roedt.ringesentralen.samtale.RingbarPerson
import no.roedt.ringesentralen.samtale.Samtale
import java.sql.Timestamp
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

@ApplicationScoped
class HistorikkService(
    private val entityManager: EntityManager,
    private val personRepository: PersonRepository
) {

    fun getMineSamtaler(userId: UserId): List<Samtale> = getSamtaler("where hypersysID='${userId.userId}'")

    fun getLagetsSamtaler(userId: UserId): List<Samtale> = getSamtaler(
        "where lokallag = ${personRepository.find("hypersysID", userId.userId).firstResult<RingbarPerson>().lokallag}"
    )

    private fun getSamtaler(whereklausul: String): List<Samtale> {
        val sql =
            "select resultat, ringerNavn, tidspunkt, kommentar, oppringtNummer, ringtNavn, merAktiv, valgkampsbrev " +
                    "from v_mineSamtaler $whereklausul"
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