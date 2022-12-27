package no.roedt.ringesentralen.samtale.start

import no.roedt.DatabaseUpdater
import no.roedt.KommuneRepository
import no.roedt.hypersys.HypersysService
import no.roedt.lokallag.Lokallag
import no.roedt.lokallag.LokallagRepository
import no.roedt.person.Person
import no.roedt.person.PersonRepository
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.enterprise.context.Dependent

@Dependent
open class NesteMedlemAaRingeFinder(
    val personRepository: PersonRepository,
    val databaseUpdater: DatabaseUpdater,
    private val hypersysService: HypersysService,
    private val kommuneRepository: KommuneRepository,
    private val lokallagRepository: LokallagRepository
) {

    fun hentIDForNesteMedlemAaRinge(ringer: Person, lokallag: Int): Int? =
        hentNestePersonAaRingeIDetteLokallaget(ringer, lokallag) ?: kommuneRepository
            .list("fylke_id=?1", ringer.fylke)
            .mapNotNull { it.lokallag_id }
            .firstOrNull { hentNestePersonAaRingeIDetteLokallaget(ringer, it) != null }

    private fun hentNestePersonAaRingeIDetteLokallaget(ringer: Person, lokallag: Int): Int? {
        val oppdaterteLokallag = oppdaterMedlemsliste(lokallag)

        val nestePersonFraDatabasen = hentNestePerson(ringer, lokallag)
        if (nestePersonFraDatabasen != null) return nestePersonFraDatabasen

        oppdaterteLokallag.forEach { lokallagRepository.persist(it) }

        return hentNestePerson(ringer, lokallag)
    }

    private fun oppdaterMedlemsliste(lokallagID: Int): Set<Lokallag> {
        val lokallag = lokallagRepository.findById(lokallagID)
        val sistOppdatert = lokallag.sistOppdatert?.atZone(ZoneId.of("UTC"))
        if (lokallag != null && (sistOppdatert.erILøpetAvSisteUka() || sistOppdatert == null)) {
            try {
                hypersysService.oppdaterLokallag()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            hypersysService.oppdaterMedlemmerILokallag(lokallag)
            lokallag.sistOppdatert = Instant.now()
            return setOf(lokallag)
        }
        return setOf()
    }

    private fun hentNestePerson(ringer: Person, lokallag: Int) = databaseUpdater.getResultList(
        """SELECT v.id FROM v_personerSomKanRinges v
                WHERE fylke = ${ringer.fylke} 
                AND lokallag=$lokallag AND hypersysID is not null 
                ORDER BY ABS(lokallag-'${ringer.lokallag}') ASC,
                hypersysID DESC,
                sisteSamtale ASC,
                v.hypersysID DESC
        """
    )
        .map { it as Int }
        .firstOrNull()
}

private fun ZonedDateTime?.erILøpetAvSisteUka(): Boolean = this?.isBefore(
    ZonedDateTime.now().minusDays(7)
) == true
