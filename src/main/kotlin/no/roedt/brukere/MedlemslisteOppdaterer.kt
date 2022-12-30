package no.roedt.brukere

import no.roedt.hypersys.HypersysService
import no.roedt.lokallag.Lokallag
import no.roedt.lokallag.LokallagRepository
import no.roedt.tidssone
import java.time.Instant
import java.time.ZonedDateTime
import javax.enterprise.context.Dependent

@Dependent
class MedlemslisteOppdaterer(
    private val lokallagRepository: LokallagRepository,
    private val hypersysService: HypersysService
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
            hypersysService.oppdaterMedlemmerILokallag(lokallag)
            lokallag.sistOppdatert = Instant.now()
            return setOf(lokallag)
        }
        return setOf()
    }
}

private fun ZonedDateTime?.erSistOppdatertFørDenSisteUka(): Boolean = this?.isBefore(
    ZonedDateTime.now().minusWeeks(1)
) == true
