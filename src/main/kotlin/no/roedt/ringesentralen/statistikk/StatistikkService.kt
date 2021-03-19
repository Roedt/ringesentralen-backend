package no.roedt.ringesentralen.statistikk

import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Roles
import no.roedt.ringesentralen.hypersys.RingerRepository
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.samtale.PersistentSamtale
import no.roedt.ringesentralen.samtale.PersistentSamtaleRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class StatistikkService(
    val databaseUpdater: DatabaseUpdater,
    val ringerRepository: RingerRepository,
    val personRepository: PersonRepository,
    val samtaleRepository: PersistentSamtaleRepository
) {

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
            personerStatistikkResponse = null)
    }

    private fun getSamtalerStatistikkResponse(): SamtalerStatistikkResponse {
        val list = get("SELECT id, displaytext FROM `resultat` ORDER BY id ASC")
            .map { it as Array<*> }
            .map { Resultattype(id = it[0] as Int, displaytext = it[1].toString()) }
            .map {
                SamtaleResultat(
                    displaytext = it.displaytext,
                    antal = samtaleRepository.find("resultat=?1", it.id).list<PersistentSamtale>().map { it.ringer }.count()
                )
            }
            .filter { it.antal > 0}

        return SamtalerStatistikkResponse(
            resultat = list,
            samtalerMedResultatSaaLangt = samtaleRepository.count("resultat!=9").toInt()
        )
    }

    private fun getRingereStatistikkResponse(): RingereStatistikkResponse =
        RingereStatistikkResponse(
            registrerteRingere = ringerRepository.count().toInt(),
            antallSomHarRingt = get("select distinct ringer from `samtale`").size,
            aktiveRingereDenSisteTimen = get("select distinct ringer from `samtale` where UNIX_TIMESTAMP(now()) - unix_timestamp(datetime) < 3600").size,
            aktiveRingereIDag = get("select distinct ringer from `samtale` where CURDATE() =  DATE(datetime)").size,
            lokaleGodkjennere = personRepository.count("groupID=8").toInt(),
            avvisteRingere = personRepository.count("groupID=5").toInt(),
            antallLokallagRingtFraTotalt = get("select distinct ringer from `samtale` c " +
                    "inner join ringer ringer on c.ringer = ringer.id " +
                    "inner join person p on ringer.personId = p.id " +
                    "inner join lokallag l on l.id = p.lokallag").size
        )

    private fun getPersonerStatistikkResponse(): PersonerStatistikkResponse = PersonerStatistikkResponse(
        antallPersonerISystemetTotalt = personRepository.count().toInt(),
        ringere = personRepository.count("groupID>3").toInt(),
        ferdigringte = personRepository.count("groupID=2 or groupID=3").toInt(),
        ringtUtenSvar = get("select 1 FROM person p inner join samtale s on s.ringt=p.id AND p.groupID=1").size,
        ikkeRingt = get("select p.* FROM person p where p.groupID < 4 and not exists (select 1 from samtale s where s.ringt=p.id)").size,
        antallLokallagMedPersonerTilknytta = get( "select distinct lokallag FROM person where lokallag is not null").size
    )

    private fun get(query: String) = databaseUpdater.getResultList(query)
}