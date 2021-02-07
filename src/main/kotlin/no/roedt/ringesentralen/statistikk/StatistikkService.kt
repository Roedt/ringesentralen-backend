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
            ringereStatistikkResponse = getRingereStatistikkResponse(),
            personerStatistikkResponse = getPersonerStatistikkResponse()
        )
    }

    private fun getSamtalerStatistikkResponse(modus: Modus) : SamtalerStatistikkResponse {
        val list = get("SELECT id, displaytext FROM v_resultatForModus where modus=${modus.id} ORDER BY id ASC")
            .map { it as Array<*> }
            .map { Resultattype(id = it[0] as Int, displaytext = it[1].toString()) }
            .map {
                SamtaleResultat(
                    displaytext = it.displaytext,
                    antal = get("SELECT ringer FROM `samtale` WHERE resultat = ${it.id}").size
                )
            }

        return SamtalerStatistikkResponse(
            resultat = list,
            samtalerMedResultatSaaLangt = get("SELECT ringer FROM `samtale` WHERE resultat != 9").size
        )
    }

    private fun getRingereStatistikkResponse(): RingereStatistikkResponse =
        RingereStatistikkResponse(
            registrerteRingere = get("SELECT 1 FROM ringer").size,
            antallSomHarRingt = get("select distinct ringer from `samtale`").size,
            aktiveRingereDenSisteTimen = get("select distinct ringer from `samtale` where UNIX_TIMESTAMP(now()) - unix_timestamp(datetime) < 3600").size,
            aktiveRingereIDag = get("select distinct ringer from `samtale` where CURDATE() =  DATE(datetime)").size,
            lokaleGodkjennere = get("select 1 FROM person WHERE groupID=8").size,
            avvisteRingere = get("select 1 FROM person WHERE groupID=5").size,
            antallLokallagRingtFraTotalt = get("select distinct ringer from `samtale` c " +
                    "inner join ringer ringer on c.ringer = ringer.id " +
                    "inner join person p on ringer.personId = p.id " +
                    "inner join lokallag l on l.id = p.lokallag").size
        )

    private fun getPersonerStatistikkResponse(): PersonerStatistikkResponse = PersonerStatistikkResponse(
        antallPersonerISystemetTotalt = get("SELECT 1 FROM person").size,
        ringere = get("SELECT 1 FROM person WHERE groupID > 3").size,
        ferdigringte = get("select 1 FROM person WHERE groupID=2 or groupID=3").size,
        ringtUtenSvar = get("select 1 FROM person WHERE sisteSamtale > 0 and groupID=1").size,
        ikkeRingt = get("select 1 FROM person WHERE sisteSamtale = 0 and groupID < 4").size,
        antallLokallagMedPersonerTilknytta = get( "select distinct lokallag FROM person where lokallag is not null").size
    )

    private fun get(query: String) = entityManager.createNativeQuery(query).resultList
}