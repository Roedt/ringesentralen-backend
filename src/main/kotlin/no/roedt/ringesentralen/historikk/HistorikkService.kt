package no.roedt.ringesentralen.historikk

import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import no.roedt.ringesentralen.samtale.Samtale
import java.math.BigInteger
import java.sql.Timestamp
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class HistorikkService(
    private val databaseUpdater: DatabaseUpdater,
    private val personRepository: PersonRepository
) {

    fun getMineSamtaler(userId: UserId, modus: Modus): List<Samtale> = getSamtaler(modus, "where hypersysID='${userId.userId}'")

    fun getLagetsSamtaler(userId: UserId, modus: Modus): List<Samtale> = getSamtaler(modus,
        "where lokallag = ${personRepository.find("hypersysID", userId.userId).firstResult<Person>().lokallag}"
    )

    private fun getSamtaler(modus: Modus, whereklausul: String): List<Samtale> {
        val sql =
            "select resultat, ringerNavn, tidspunkt, kommentar, oppringtNummer, ringtNavn, merAktiv, valgkampsbrev " +
                    "from v_mineSamtaler $whereklausul and modus='${modus.name}'"
        return databaseUpdater.getResultList(sql)
            .map { it as Array<*> }
            .map {
                Samtale(
                    resultat = it[0] as String,
                    ringer = it[1] as String,
                    tidspunkt = (it[2] as Timestamp).toString(),
                    kommentar = (it[3] ?: "") as String,
                    ringtNummer = (it[4] ?: "Ukjent") as String,
                    ringtNavn = it[5] as String
                )
            }
    }

    fun tellMineSamtaler(userId: UserId): Int =
        databaseUpdater.getResultList(
            "select count(1) from samtale s inner join ringer r on s.ringer=r.id inner join person p on p.id=r.personId where hypersysID='${userId.userId}'"
        )
            .first()
            .let { it as BigInteger }
            .toInt()
}