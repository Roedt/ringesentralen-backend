package no.roedt.ringesentralen.statistikk

import no.roedt.ringesentralen.Modus
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

@ApplicationScoped
class StatistikkService(val entityManager: EntityManager) {

    fun getStatistikk(): StatistikkResponse {
        val modus = Modus.Korona
        return StatistikkResponse(
            samtalerStatistikkResponse = getSamtalerStatistikkResponse(modus),
            ringereStatistikkResponse = getRingereStatistikkResponse(modus),
            personerStatistikkResponse = getPersonerStatistikkResponse(modus)
        )
    }

    private fun getSamtalerStatistikkResponse(modus: Modus) : SamtalerStatistikkResponse {
        val list = get("SELECT id, displaytext FROM v_resultatForModus where modus=${modus.id} ORDER BY id ASC")
            .map { it as Array<Any> }
            .map { Resultattype(id = it[0] as Int, displaytext = it[1].toString()) }
            .map {
                SamtaleResultat(
                    displaytext = it.displaytext,
                    antal = get("SELECT callerPhone FROM `call` WHERE result = ${it.id} AND typeCall = 1").size
                )
            }

        return SamtalerStatistikkResponse(
            resultat = list,
            samtalerMedResultatSaaLangt = get("SELECT callerPhone FROM `call` WHERE typeCall = 1 and result != 9").size
        )
    }

    private fun getRingereStatistikkResponse(modus: Modus): RingereStatistikkResponse =
        RingereStatistikkResponse(
            registrerteRingere = get("SELECT 1 FROM ringer").size,
            antallSomHarRingt = get("select distinct callerPhone from `call`").size,
            aktiveRingereDenSisteTimen = get("select distinct callerPhone from `call` where UNIX_TIMESTAMP(now()) - unix_timestamp(datetime) < 3600").size,
            aktiveRingereIDag = get("select distinct callerPhone from `call` where CURDATE() =  DATE(datetime)").size,
            lokaleGodkjennere = get("select 1 FROM person WHERE groupID=8").size,
            avvisteRingere = get("select 1 FROM person WHERE groupID=5").size,
            antallLokallagRingtFraTotalt = get("select distinct callerPhone from `call` c inner join person p on p.phone = c.callerPhone inner join lokallag l on l.id = p.lokallag").size
        )

    private fun getPersonerStatistikkResponse(modus: Modus): PersonerStatistikkResponse = PersonerStatistikkResponse(
        antallPersonerISystemetTotalt = get("SELECT 1 FROM person").size,
        ringere = get("SELECT 1 FROM person WHERE groupID > 3").size,
        ferdigringte = get("select 1 FROM person WHERE groupID=2 or groupID=3").size,
        ringtUtenSvar = get("select 1 FROM person WHERE lastCall > 0 and groupID=1").size,
        ikkeRingt = get("select 1 FROM person WHERE lastCall = 0 and groupID < 4").size,
        antallLokallagMedPersonerTilknytta = get( "select distinct lokallag FROM person where lokallag is not null").size
    )

    private fun get(query: String) = entityManager.createNativeQuery(query).resultList
}