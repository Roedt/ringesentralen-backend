package no.roedt.ringesentralen.samtale

import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.KommuneRepository
import no.roedt.ringesentralen.hypersys.HypersysService
import no.roedt.ringesentralen.hypersys.ModelConverter
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import javax.enterprise.context.Dependent

@Dependent
open class NesteMedlemAaRingeFinder(
    val personRepository: PersonRepository,
    val databaseUpdater: DatabaseUpdater,
    private val hypersysService: HypersysService,
    private val modelConverter: ModelConverter,
    private val kommuneRepository: KommuneRepository
) {

    fun hentIDForNesteMedlemAaRinge(ringer: Person, userId: UserId, lokallag: Int): Any? {
        val nestePersonIEgetLokallag = hentNestePersonAaRingeIDetteLokallaget(
            ringer,
            hypersysService.getLokallag(userId)
        )
        if (nestePersonIEgetLokallag != null) return nestePersonIEgetLokallag

        return kommuneRepository.list("fylke_id=?1", ringer.fylke)
            .map { it.lokallag_id }
            .firstOrNull { hentNestePersonAaRingeIDetteLokallaget(ringer, it) != null }
    }

    private fun hentNestePersonAaRingeIDetteLokallaget(ringer: Person, lokallag: Int?): Any? {
        if (lokallag == null) return null
        val nestePersonFraDatabasen = hentNestePerson(ringer, lokallag)
        if (nestePersonFraDatabasen != null) return nestePersonFraDatabasen

        hentMedlemmerFraLokallag(hypersysService.convertToHypersysLokallagId(lokallag))

        return hentNestePerson(ringer, lokallag)
    }

    private fun hentMedlemmerFraLokallag(hypersysLokallagId: Int?) =
        hypersysService.getMedlemmer(hypersysLokallagId)
            .filter { medlem -> personRepository.find("hypersysID", medlem["member_id"]).count() == 0L }
            .map { modelConverter.convertMembershipToPerson(it) }
            .filter { it.telefonnummer != null }
            .forEach { personRepository.save(it) }


    private fun hentNestePerson(ringer: Person, lokallag: Int) = databaseUpdater.getResultList(
        """SELECT v.id FROM v_personerSomKanRinges v
                WHERE fylke = ${ringer.fylke} 
                AND lokallag=$lokallag AND hypersysID is not null 
                ORDER BY ABS(lokallag-'${ringer.lokallag}') ASC,
                v.hypersysID DESC""")
        .firstOrNull()


}