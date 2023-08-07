package no.roedt.brukere

import jakarta.enterprise.context.Dependent
import no.roedt.hypersys.HypersysService
import no.roedt.hypersys.externalModel.membership.Membership
import no.roedt.lokallag.Lokallag
import no.roedt.lokallag.LokallagService
import no.roedt.person.Oppdateringskilde
import no.roedt.person.Person
import no.roedt.person.PersonService
import no.roedt.tidssone
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.time.Instant
import java.time.ZonedDateTime

@Dependent
class MedlemslisteOppdaterer(
    private val lokallagService: LokallagService,
    private val hypersysService: HypersysService,
    private val personService: PersonService,
    private val tidligereMedlemSletter: TidligereMedlemSletter,
    @ConfigProperty(
        name = "overstyrVentingFoerOppdateringMotHypersys",
        defaultValue = "false"
    ) val overstyrVenting: Boolean
) {

    fun oppdaterMedlemsliste(lokallagID: Int): Set<Lokallag> {
        val lokallag = lokallagService.findById(lokallagID)
        val sistOppdatert = lokallag.sistOppdatert?.atZone(tidssone())
        val sistOppdatertTilsierOppdatere =
            overstyrVenting || sistOppdatert.erSistOppdatertFørDenSisteUka() || sistOppdatert == null
        if (sistOppdatertTilsierOppdatere) {
            try {
                hypersysService.oppdaterLokallag()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            oppdaterMedlemmer(lokallag)
            lokallag.sistOppdatert = Instant.now()
            return setOf(lokallag)
        }
        return setOf()
    }

    private fun oppdaterMedlemmer(lokallag: Lokallag) {
        val oppdatertMedlemsliste = hypersysService.hentOppdatertMedlemslisteForLokallag(lokallag)
        val partitionNyEksisterende = oppdatertMedlemsliste
            .partition { medlem -> !personService.harMedlemMedHypersysID(medlem.member_id) }
        leggTilNyMedlemFraHypersys(partitionNyEksisterende)
        oppdaterEksisterendeMedlemmer(partitionNyEksisterende, lokallag)
        oppdaterMedlemmerSomIkkeErILagetIHypersysLenger(partitionNyEksisterende, lokallag)
    }

    private fun leggTilNyMedlemFraHypersys(partitionNyEksisterende: Pair<List<Membership>, List<Membership>>) {
        partitionNyEksisterende
            .first
            .map { hypersysService.convertMembershipToPerson(it) }
            .filter { it.telefonnummer != null }
            .forEach { personService.save(it, Oppdateringskilde.Hypersys) }
    }

    private fun oppdaterEksisterendeMedlemmer(
        partitionNyEksisterende: Pair<List<Membership>, List<Membership>>,
        lokallag: Lokallag
    ) {
        partitionNyEksisterende
            .second
            .map { Pair(it, personService.finnFraHypersysId(it.member_id)) }
            .filter { it.second.count() > 0 }
            .map {
                hypersysService.konverterTilOppdatering(
                    it.first,
                    lokallag,
                    it.second.firstResult()
                )
            }
            .forEach { personService.oppdater(it) }
    }

    private fun oppdaterMedlemmerSomIkkeErILagetIHypersysLenger(
        partitionNyEksisterende: Pair<List<Membership>, List<Membership>>,
        lokallag: Lokallag
    ) {
        val ikkeIDetteLagetIHypersys = personService.hentMedlemmerILokallag(lokallag.id)
            .filter { erIkkeILaget(lokallag, it, partitionNyEksisterende.first) }
            .filter { erIkkeILaget(lokallag, it, partitionNyEksisterende.second) }
        val deltIMedlemIkkeMedlem = ikkeIDetteLagetIHypersys
            .map { Pair(it, hypersysService.hentPerson(it)) }
            .partition { it.second.is_member }
        // Medlemmer i andre lag blir automatisk flytta over når vi hentar inn medlemslista for det nye laget deira.
        // Før den tid er det ikkje heilt godt å seie kva vi bør gjera, for HS har tilsynelatande ikkje noko endepunkt som gir lag gitt brukarid, og å iterere gjennom alt blir for tullete
        // Kanskje vi kan lage eit "jukse-lokallag" som heiter noko a la "Har bytta lokallag i Hypersys, men ikkje oppdatert her enno"
//        val medlemmerIAndreLag = deltIMedlemIkkeMedlem.first
        println("Fant ${deltIMedlemIkkeMedlem.second.size} tidligere medlemmer i ${lokallag.id} som ikke lenger er medlem")
        deltIMedlemIkkeMedlem.second.map { it.first }.forEach { tidligereMedlemSletter.slett(it) }
    }

    private fun erIkkeILaget(lokallag: Lokallag, person: Person, memberships: List<Membership>) =
        !memberships
            .filter { it.organisation == lokallag.navn }
            .map { it.member_id }
            .contains(person.hypersysID)
}

private fun ZonedDateTime?.erSistOppdatertFørDenSisteUka(): Boolean = this?.isBefore(
    ZonedDateTime.now().minusWeeks(1)
) == true
