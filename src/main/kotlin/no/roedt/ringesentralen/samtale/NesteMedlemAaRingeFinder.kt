package no.roedt.ringesentralen.samtale

import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Kommune
import no.roedt.ringesentralen.KommuneRepository
import no.roedt.ringesentralen.hypersys.HypersysService
import no.roedt.ringesentralen.hypersys.ModelConverter
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import org.eclipse.microprofile.jwt.JsonWebToken
import javax.enterprise.context.Dependent

@Dependent
open class NesteMedlemAaRingeFinder(
    val personRepository: PersonRepository,
    val databaseUpdater: DatabaseUpdater,
    val hypersysService: HypersysService,
    val modelConverter: ModelConverter,
    val kommuneRepository: KommuneRepository
) {

    fun hentIDForNesteMedlemAaRinge(ringer: Person, userId: UserId, jwt: JsonWebToken): Any? {
        val nestePersonIEgetLokallag = hentNestePersonAaRingeIDetteLokallaget(ringer, jwt, hypersysService.getLokallag(userId))
        if (nestePersonIEgetLokallag != null) return nestePersonIEgetLokallag

        val lokallagIFylket = kommuneRepository.find("fylke_id=?1", ringer.fylke)
            .list<Kommune>()
            .map { it.lokallag_id }

        for (lokallag in lokallagIFylket) {
            val personAaRinge = hentNestePersonAaRingeIDetteLokallaget(ringer, jwt, lokallag)
            if (personAaRinge != null) return personAaRinge
        }

        return null
    }

    private fun hentNestePersonAaRingeIDetteLokallaget(ringer: Person, jwt: JsonWebToken, lokallag: Int?): Any? {
        if (lokallag == null) return null
        val hypersysQuery = "AND lokallag=$lokallag AND hypersysID is not null "
        val nestePersonFraDatabasen = hentNestePerson(ringer, hypersysQuery)
        if (nestePersonFraDatabasen != null) return nestePersonFraDatabasen

        hentMedlemmerFraLokallag(jwt, hypersysService.convertToHypersysLokallagId(lokallag))

        return hentNestePerson(ringer, hypersysQuery)
    }

    private fun hentMedlemmerFraLokallag(jwt: JsonWebToken, hypersysLokallagId: Int?) =
        hypersysService.getMedlemmer(hypersysLokallagId, jwt)
            .filter { medlem -> personRepository.find("hypersysID", medlem["member_id"]).count() == 0L }
            .map { modelConverter.convertMembershipToPerson(it) }
            .forEach { personRepository.save(it) }


    private fun hentNestePerson(ringer: Person, hypersysQuery: String) = databaseUpdater.getResultList(
        "SELECT v.id FROM v_personerSomKanRinges v " +
                "WHERE fylke = ${ringer.fylke} " +
                hypersysQuery +
                " ORDER BY ABS(lokallag-'${ringer.lokallag}') ASC, " +
                "v.hypersysID DESC")
        .firstOrNull()


}