package no.roedt.ringesentralen.statistikk

import no.roedt.person.RingesentralenGroupID
import no.roedt.DatabaseUpdater
import no.roedt.ringesentralen.Roles
import no.roedt.ringesentralen.samtale.resultat.Resultat
import java.sql.Timestamp
import java.time.Instant
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class StatistikkService(val databaseUpdater: DatabaseUpdater) {

    fun getStatistikk(groups: Set<String>): StatistikkResponse {
        return if (groups.contains(Roles.admin)) {
            StatistikkResponse(
                samtalerStatistikkResponse = getSamtalerStatistikkResponse(),
                ringereStatistikkResponse = getRingereStatistikkResponse(),
                personerStatistikkResponse = getPersonerStatistikkResponse()
            )
        } else StatistikkResponse(
            samtalerStatistikkResponse = getSamtalerStatistikkResponse(),
            ringereStatistikkResponse = null,
            personerStatistikkResponse = null
        )
    }

    private fun getSamtalerStatistikkResponse(): SamtalerStatistikkResponse {
        val list = get("SELECT id, displaytext FROM `resultat` ")
            .map { it as Array<*> }
            .map { Resultattype(id = it[0] as Int, displaytext = it[1].toString()) }
            .map {
                SamtaleResultat(
                    displaytext = it.displaytext,
                    antal = get("SELECT ringer FROM `samtale` WHERE resultat = ${it.id}").size
                )
            }
            .filter { it.antal > 0 }

        return SamtalerStatistikkResponse(
            resultat = list,
            samtalerMedResultatSaaLangt = get("SELECT ringer FROM `samtale` WHERE resultat!=${Resultat.Samtale_startet.nr}").size
        )
    }

    private fun getRingereStatistikkResponse(): RingereStatistikkResponse =
        RingereStatistikkResponse(
            registrerteRingere = get("SELECT 1 FROM ringer").size,
            antallSomHarRingt = get("select distinct ringer from `samtale`").size,
            aktiveRingereDenSisteTimen = get("select distinct ringer from `samtale` where UNIX_TIMESTAMP(now()) - unix_timestamp(datetime) < 3600").size,
            aktiveRingereIDag = get("select distinct ringer from `samtale` where CURDATE() =  DATE(datetime)").size,
            lokaleGodkjennere = get("select 1 FROM person WHERE groupID=${RingesentralenGroupID.LokalGodkjenner.nr}").size,
            avvisteRingere = get("select 1 FROM person WHERE groupID=${RingesentralenGroupID.AvslaattRinger.nr}").size,
            antallLokallagRingtFraTotalt = get(
                "select distinct ringer from `samtale` c " +
                    "inner join ringer ringer on c.ringer = ringer.id " +
                    "inner join person p on ringer.personId = p.id " +
                    "inner join lokallag l on l.id = p.lokallag"
            ).size
        )

    private fun getPersonerStatistikkResponse(): PersonerStatistikkResponse = PersonerStatistikkResponse(
        antallPersonerISystemetTotalt = get("SELECT 1 FROM person").size,
        ringere = get("SELECT 1 FROM person WHERE groupID in (${RingesentralenGroupID.GodkjentRinger.nr}, ${RingesentralenGroupID.GodkjentRingerMedlemmer.nr}, ${RingesentralenGroupID.LokalGodkjenner.nr}, ${RingesentralenGroupID.Admin.nr} )").size,
        ferdigringte = get("select 1 FROM person WHERE groupID=${RingesentralenGroupID.Ferdigringt.nr} or groupID=${RingesentralenGroupID.Slett.nr}").size,
        ringtUtenSvar = get("select 1 FROM person p inner join samtale s on s.ringt=p.id AND p.groupID=${RingesentralenGroupID.KlarTilAaRinges.nr}").size,
        ikkeRingt = get("select 1 FROM person p where p.groupID < ${RingesentralenGroupID.UgodkjentRinger.nr} and not exists (select 1 from samtale s where s.ringt=p.id)").size,
        antallLokallagMedPersonerTilknytta = get("select distinct lokallag FROM person where lokallag is not null").size
    )

    private fun get(query: String) = databaseUpdater.getResultList(query)

    fun getRingtMest(hypersysId: Int): RingtFlestStatistikk = RingtFlestStatistikk(
        jegHarRingt = mineRingte(hypersysId),
        maksRingt = personSomHarRingtFlest(),
        samtalerGjennomfoertILaget = samtalerGjennomfoertILaget(hypersysId),
        antallRingereILaget = antallRingereILaget(hypersysId)
    )

    private fun mineRingte(hypersysId: Int): Int = get(
        """SELECT 1 FROM samtale samtale 
                INNER JOIN ringer ringer on samtale.ringer=ringer.id 
                INNER JOIN person ringerPerson on ringerPerson.id=ringer.personId 
                WHERE ringerPerson.hypersysID=$hypersysId 
                and samtale.resultat != ${Resultat.Samtale_startet.nr} 
                and samtale.ringt != ringerPerson.id
        """
    ).size

    private fun personSomHarRingtFlest(): Int = get(
        """SELECT count(1) FROM samtale samtale 
            INNER JOIN ringer ringer on samtale.ringer=ringer.id 
            INNER JOIN person ringerPerson on ringerPerson.id=ringer.personId
            WHERE samtale.resultat != 9
            and samtale.ringt != ringerPerson.id
            group by ringer.id
            order by count(1) desc limit 1
        """
    )[0].toString().toInt()

    private fun samtalerGjennomfoertILaget(hypersysId: Int): Int =
        get(
            """SELECT 1 FROM samtale samtale 
                INNER JOIN ringer ringer on samtale.ringer=ringer.id 
                INNER JOIN person ringerPerson on ringerPerson.id=ringer.personId            
                WHERE ringerPerson.lokallag=(select lokallag from person where hypersysID=$hypersysId) 
                and samtale.resultat != ${Resultat.Samtale_startet.nr} 
                and samtale.ringt != ringerPerson.id
            """
        ).size

    private fun antallRingereILaget(hypersysId: Int): Int =
        get(
            """SELECT 1 FROM ringer ringer
                INNER JOIN person person on ringer.personId = person.id
                WHERE person.lokallag =(select lokallag from person where hypersysID=$hypersysId)
            """
        ).size

    fun lodd(fra: Instant, til: Instant): List<LoddStatistikk> =
        get(
            """
                SELECT ringerPerson.fornavn, ringerPerson.etternavn, lokallag.navn, count(1)
                FROM samtale samtale
                INNER JOIN ringer ringer on ringer.id = samtale.ringer
                INNER JOIN person ringerPerson on ringer.personId = ringerPerson.id
                INNER JOIN lokallag lokallag on lokallag.id = ringerPerson.lokallag
                AND samtale.datetime >= '${Timestamp.from(fra)}'
                AND samtale.datetime <= '${Timestamp.from(til)}'
                AND samtale.resultat != ${Resultat.Samtale_startet.nr}
                GROUP BY ringerPerson.id
            """.trimIndent()
        )
            .map { it as Array<*> }
            .map {
                LoddStatistikk(
                    fornavn = it[0].toString(),
                    etternavn = it[1].toString(),
                    lokallag = it[2].toString(),
                    antallSamtaler = it[3].toString().toInt()
                )
            }
}
