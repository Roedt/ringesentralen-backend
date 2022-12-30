package no.roedt.brukere

import no.roedt.hypersys.HypersysService
import no.roedt.hypersys.externalModel.membership.Membership
import no.roedt.lokallag.Lokallag
import no.roedt.lokallag.LokallagRepository
import no.roedt.person.Oppdateringskilde
import no.roedt.person.PersonRepository
import no.roedt.tidssone
import java.time.Instant
import java.time.ZonedDateTime
import javax.enterprise.context.Dependent

@Dependent
class MedlemslisteOppdaterer(
    private val lokallagRepository: LokallagRepository,
    private val hypersysService: HypersysService,
    private val personRepository: PersonRepository
) {

    fun oppdaterMedlemsliste(lokallagID: Int): Set<Lokallag> {
        val lokallag = lokallagRepository.findById(lokallagID)
        val sistOppdatert = lokallag.sistOppdatert?.atZone(tidssone())
        if (lokallag != null && (sistOppdatert.erSistOppdatertFørDenSisteUka() || sistOppdatert == null)) {
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
            .partition { medlem -> personRepository.find("hypersysID", medlem.member_id).count() == 0L }
        leggTilNyMedlemFraHypersys(partitionNyEksisterende)
        oppdaterEksisterendeMedlemmer(partitionNyEksisterende, lokallag)
    }

    private fun leggTilNyMedlemFraHypersys(partitionNyEksisterende: Pair<List<Membership>, List<Membership>>) {
        partitionNyEksisterende
            .first
            .map { hypersysService.convertMembershipToPerson(it) }
            .filter { it.telefonnummer != null }
            .forEach { personRepository.save(it, Oppdateringskilde.Hypersys) }
    }

    private fun oppdaterEksisterendeMedlemmer(
        partitionNyEksisterende: Pair<List<Membership>, List<Membership>>,
        lokallag: Lokallag
    ) {
        partitionNyEksisterende
            .second
            .map { Pair(it, personRepository.find("hypersysID", it.member_id)) }
            .filter { it.second.count() > 0 }
            .map {
                hypersysService.konverterTilOppdatering(
                    it.first,
                    lokallag,
                    it.second.firstResult()
                )
            }
            .forEach { personRepository.oppdater(it) }
        // TODO: Mekanisme ca her for å slette dei som ikkje lenger er med i laget
        // Eventuelt noko lurt for å anonymisere eller noko
        // Kanskje vi også eksplisitt skal sjekke dei mot HS for å sjå om dei berre har bytta lag
    }
}

private fun ZonedDateTime?.erSistOppdatertFørDenSisteUka(): Boolean = this?.isBefore(
    ZonedDateTime.now().minusWeeks(1)
) == true
