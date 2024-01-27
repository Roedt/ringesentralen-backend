package no.roedt.ringesentralen.statistikk

import jakarta.enterprise.context.ApplicationScoped
import no.roedt.brukere.GenerellRolle
import java.time.Instant

@ApplicationScoped
class StatistikkService(val repository: StatistikkRepository) {
    fun getStatistikk(groups: Set<String>): StatistikkResponse {
        return if (groups.contains(GenerellRolle.ADMIN)) {
            StatistikkResponse(
                samtalerStatistikkResponse = repository.getSamtalerStatistikkResponse(),
                ringereStatistikkResponse = repository.getRingereStatistikkResponse(),
                personerStatistikkResponse = repository.getPersonerStatistikkResponse(),
                ringteIValkampen2023 = repository.ringteIValkampen2023()
            )
        } else {
            StatistikkResponse(
                samtalerStatistikkResponse = repository.getSamtalerStatistikkResponse(),
                ringereStatistikkResponse = null,
                personerStatistikkResponse = null
            )
        }
    }

    fun getRingtMest(hypersysId: Int): RingtFlestStatistikk =
        RingtFlestStatistikk(
            jegHarRingt = repository.mineRingte(hypersysId),
            maksRingt = repository.personSomHarRingtFlest(),
            samtalerGjennomfoertILaget = repository.samtalerGjennomfoertILaget(hypersysId),
            antallRingereILaget = repository.antallRingereILaget(hypersysId)
        )

    fun lodd(
        fra: Instant,
        til: Instant
    ): List<LoddStatistikk> = repository.lodd(fra, til)
}
