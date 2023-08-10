package no.roedt.ringesentralen.statistikk

import jakarta.enterprise.context.Dependent
import jakarta.persistence.EntityManager
import no.roedt.list
import no.roedt.ringesentralen.brukere.RingesentralenGroupID
import no.roedt.ringesentralen.samtale.resultat.Resultat
import java.sql.Timestamp
import java.time.Instant

@Dependent
class StatistikkRepository(internal val entityManager: EntityManager) {
    fun getSamtalerStatistikkResponse(): SamtalerStatistikkResponse {
        val list = entityManager.list("SELECT id, displaytext FROM `resultat` ")
            .map { it as Array<*> }
            .map { Resultattype(id = it[0] as Int, displaytext = it[1].toString()) }
            .map {
                SamtaleResultat(
                    displaytext = it.displaytext,
                    antal = entityManager.list("SELECT ringer FROM `samtale` WHERE resultat = ${it.id}").size
                )
            }
            .filter { it.antal > 0 }

        return SamtalerStatistikkResponse(
            resultat = list,
            samtalerMedResultatSaaLangt = entityManager.list("SELECT ringer FROM `samtale` WHERE resultat!=${Resultat.Samtale_startet.nr}").size
        )
    }

    fun getRingereStatistikkResponse(): RingereStatistikkResponse =
        RingereStatistikkResponse(
            registrerteRingere = entityManager.list("SELECT 1 FROM ringer").size,
            antallSomHarRingt = entityManager.list("select distinct ringer from `samtale`").size,
            aktiveRingereDenSisteTimen = entityManager.list("select distinct ringer from `samtale` where UNIX_TIMESTAMP(now()) - unix_timestamp(datetime) < 3600").size,
            aktiveRingereIDag = entityManager.list("select distinct ringer from `samtale` where CURDATE() =  DATE(datetime)").size,
            lokaleGodkjennere = entityManager.list("select 1 FROM person WHERE groupID=${RingesentralenGroupID.LokalGodkjenner.nr}").size,
            avvisteRingere = entityManager.list("select 1 FROM person WHERE groupID=${RingesentralenGroupID.AvslaattRinger.nr}").size,
            antallLokallagRingtFraTotalt = entityManager.list(
                "select distinct ringer from `samtale` c " +
                    "inner join ringer ringer on c.ringer = ringer.id " +
                    "inner join person p on ringer.personId = p.id " +
                    "inner join lokallag l on l.id = p.lokallag"
            ).size
        )

    fun getPersonerStatistikkResponse(): PersonerStatistikkResponse = PersonerStatistikkResponse(
        antallPersonerISystemetTotalt = entityManager.list("SELECT 1 FROM person").size,
        ringere = entityManager.list("SELECT 1 FROM person WHERE groupID in (${RingesentralenGroupID.GodkjentRinger.nr}, ${RingesentralenGroupID.GodkjentRingerMedlemmer.nr}, ${RingesentralenGroupID.LokalGodkjenner.nr}, ${RingesentralenGroupID.Admin.nr} )").size,
        ferdigringte = entityManager.list("select 1 FROM person WHERE groupID=${RingesentralenGroupID.Ferdigringt.nr} or groupID=${RingesentralenGroupID.Slett.nr}").size,
        ringtUtenSvar = entityManager.list("select 1 FROM person p inner join samtale s on s.ringt=p.id AND p.groupID=${RingesentralenGroupID.KlarTilAaRinges.nr}").size,
        ikkeRingt = entityManager.list("select 1 FROM person p where p.groupID < ${RingesentralenGroupID.UgodkjentRinger.nr} and not exists (select 1 from samtale s where s.ringt=p.id)").size,
        antallLokallagMedPersonerTilknytta = entityManager.list("select distinct lokallag FROM person where lokallag is not null").size
    )

    internal fun mineRingte(hypersysId: Int): Int = entityManager.list(
        """SELECT 1 FROM samtale samtale 
                INNER JOIN ringer ringer on samtale.ringer=ringer.id 
                INNER JOIN person ringerPerson on ringerPerson.id=ringer.personId 
                WHERE ringerPerson.hypersysID=$hypersysId 
                and samtale.resultat != ${Resultat.Samtale_startet.nr} 
                and samtale.ringt != ringerPerson.id
        """
    ).size

    internal fun personSomHarRingtFlest(): Int = entityManager.list(
        """SELECT count(1) FROM samtale samtale 
            INNER JOIN ringer ringer on samtale.ringer=ringer.id 
            INNER JOIN person ringerPerson on ringerPerson.id=ringer.personId
            WHERE samtale.resultat != 9
            and samtale.ringt != ringerPerson.id
            group by ringer.id
            order by count(1) desc limit 1
        """
    )[0].toString().toInt()

    internal fun samtalerGjennomfoertILaget(hypersysId: Int): Int =
        entityManager.list(
            """SELECT 1 FROM samtale samtale 
                INNER JOIN ringer ringer on samtale.ringer=ringer.id 
                INNER JOIN person ringerPerson on ringerPerson.id=ringer.personId            
                WHERE ringerPerson.lokallag=(select lokallag from person where hypersysID=$hypersysId) 
                and samtale.resultat != ${Resultat.Samtale_startet.nr} 
                and samtale.ringt != ringerPerson.id
            """
        ).size

    internal fun antallRingereILaget(hypersysId: Int): Int =
        entityManager.list(
            """SELECT 1 FROM ringer ringer
                INNER JOIN person person on ringer.personId = person.id
                WHERE person.lokallag =(select lokallag from person where hypersysID=$hypersysId)
            """
        ).size

    fun lodd(fra: Instant, til: Instant): List<LoddStatistikk> =
        entityManager.list(
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

    fun ringteIValkampen2023(): Map<String, Int> =
        entityManager.list(
            """
                SELECT lokallag.navn, count(1) FROM samtale samtale 
                INNER JOIN ringer ringer on samtale.ringer=ringer.id 
                INNER JOIN person ringerPerson on ringerPerson.id=ringer.personId
                INNER JOIN lokallag lokallag on lokallag.id = ringerPerson.lokallag
                INNER JOIN ringesesjon sesjon on sesjon.id = samtale.ringesesjon
                WHERE samtale.resultat = ${Resultat.Samtale_startet.nr}
                and samtale.ringt != ringerPerson.id
                and sesjon.tekst = 'Valkamp 2023'
                group by ringerPerson.lokallag
                order by count(1) desc
            """.trimIndent()
        )
            .map { it as Array<*> }
            .sortedBy { it[1].toString().toInt() }
            .associate { it[0].toString() to it[1].toString().toInt() }
}
