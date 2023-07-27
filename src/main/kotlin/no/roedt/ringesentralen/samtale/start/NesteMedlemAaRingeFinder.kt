package no.roedt.ringesentralen.samtale.start

import jakarta.enterprise.context.Dependent
import no.roedt.brukere.MedlemslisteOppdaterer
import no.roedt.kommune.KommuneService
import no.roedt.lokallag.LokallagService
import no.roedt.person.Person

@Dependent
open class NesteMedlemAaRingeFinder(
    val repository: NesteAaRingeRepository,
    private val kommuneService: KommuneService,
    private val lokallagService: LokallagService,
    private val medlemslisteOppdaterer: MedlemslisteOppdaterer
) {

    fun hentIDForNesteMedlemAaRinge(ringer: Person, lokallag: Int): Int? =
        hentNestePersonAaRingeIDetteLokallaget(ringer, lokallag) ?: kommuneService
            .hent(ringer.fylke)
            .firstOrNull { hentNestePersonAaRingeIDetteLokallaget(ringer, it) != null }

    private fun hentNestePersonAaRingeIDetteLokallaget(ringer: Person, lokallag: Int): Int? {
        val oppdaterteLokallag = medlemslisteOppdaterer.oppdaterMedlemsliste(lokallag)

        val nestePersonFraDatabasen = hentNestePerson(ringer, lokallag)
        if (nestePersonFraDatabasen != null) return nestePersonFraDatabasen

        oppdaterteLokallag.forEach { lokallagService.persist(it) }

        return hentNestePerson(ringer, lokallag)
    }

    private fun hentNestePerson(ringer: Person, lokallag: Int) = repository.hentNesteMedlem(ringer.fylke, lokallag, ringer.lokallag)
}
