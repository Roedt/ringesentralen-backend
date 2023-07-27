package no.roedt.ringesentralen.historikk

import jakarta.enterprise.context.ApplicationScoped
import no.roedt.person.UserId
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.samtale.OppfoelgingValg21Service
import no.roedt.ringesentralen.samtale.Samtale
import no.roedt.skrivUt
import java.math.BigInteger
import java.sql.Timestamp

@ApplicationScoped
class HistorikkService(
    private val oppfoelgingValg21Service: OppfoelgingValg21Service,
    internal val repository: HistorikkRepository
) {

    fun getMineSamtaler(userId: UserId, modus: Modus): List<Samtale> =
        repository.hentMineSamtaler(userId, modus)
            .map { it as Array<*> }
            .map { tilSamtale(it) }

    fun getLagetsSamtaler(userId: UserId, modus: Modus, lokallag: Int): List<Samtale> =
        repository.hentLagetsSamtaler(modus, lokallag)
            .map { it as Array<*> }
            .map { tilSamtale(it) }

    private fun tilSamtale(it: Array<*>) = Samtale(
        resultat = it[0] as String,
        ringer = it[1] as String,
        tidspunkt = (it[2] as Timestamp).skrivUt(),
        kommentar = (it[3] ?: "") as String,
        ringtNummer = (it[4] ?: "Ukjent") as String,
        ringtNavn = it[5] as String,
        oppfoelging = it[6]?.toString()
            ?.let { i -> if (i != "null") oppfoelgingValg21Service.findById(i.toInt()) else null }
    )

    fun tellMineSamtaler(userId: UserId): Int =
        repository.tellMineSamtaler(userId)
            .first()
            .let { it as BigInteger }
            .toInt()
}
