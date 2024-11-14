package no.roedt.ringesentralen.samtale.start

import io.mockk.called
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import no.roedt.Kilde
import no.roedt.kommune.Kommune
import no.roedt.lokallag.Lokallag
import no.roedt.lokallag.LokallagService
import no.roedt.person.Person
import no.roedt.person.PersonService
import no.roedt.person.UserId
import no.roedt.postnummer.Postnummer
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.samtale.OppfoelgingValg21Service
import no.roedt.ringesentralen.samtale.oppslag.OppslagService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NestePersonAaRingeFinderTest {
    private val personService: PersonService = mockk()
    private val nesteAaRingeRepository: NesteAaRingeRepository = mockk()
    private val oppfoelgingValg21Service: OppfoelgingValg21Service = mockk()
    private val lokallagService: LokallagService = mockk()
    private val oppslagService: OppslagService = mockk()
    private val nesteMedlemAaRingeFinder: NesteMedlemAaRingeFinder = mockk()

    private val nestePersonAaRingeFinder =
        NestePersonAaRingeFinder(
            personService = personService,
            nesteAaRingeRepository = nesteAaRingeRepository,
            oppslagService = oppslagService,
            oppfoelgingValg21Service = oppfoelgingValg21Service,
            nesteMedlemAaRingeFinder = nesteMedlemAaRingeFinder,
            lokallagService = lokallagService,
            nyligeOppslagCache = NyligeOppslagCache()
        )

    @BeforeEach
    fun setup() {
        every { personService.getPerson(any()) } returns lagPerson("Peder", "Ås", "123")
    }

    @Test
    fun `hentar neste person aa ringe`() {
        every { nesteAaRingeRepository.hentNesteIkkemedlem(any(), any()) } returns listOf(1234)
        every { personService.findById(any()) } returns lagPerson("Peder", "Ås", "123")
        every { lokallagService.findById(any()) } returns Lokallag(navn = "Lokallag 1", hypersysID = null, fylke = 1, sistOppdatert = null)
        every { nesteAaRingeRepository.getTidlegareSamtalarMedDennePersonen(any()) } returns listOf<Array<*>>()
        every { oppslagService.persist(any()) } just runs

        nestePersonAaRingeFinder.hentNestePersonAaRinge(
            AutentisertNestePersonAaRingeRequest(
                userId = UserId(userId = 1),
                modus = Modus.velgere,
                lokallag = 1,
                roller = setOf()
            )
        )

        verify { nesteAaRingeRepository.hentNesteIkkemedlem(any(), any()) }
        verify { personService.findById(1234) }
    }

    @Test
    fun `returnerer utan svar viss ingen fleire aa ringe no`() {
        val emptyList: List<Long> = listOf()
        every { nesteAaRingeRepository.hentNesteIkkemedlem(any(), any()) } returns emptyList

        verify { personService wasNot called }

        nestePersonAaRingeFinder.hentNestePersonAaRinge(
            AutentisertNestePersonAaRingeRequest(
                userId = UserId(userId = 1),
                modus = Modus.velgere,
                lokallag = 1,
                roller = setOf()
            )
        )
        verify { nesteAaRingeRepository.hentNesteIkkemedlem(any(), any()) }
    }

    private fun lagPerson(
        fornavn: String,
        etternavn: String,
        telefonnummer: String
    ) = Person(
        hypersysID = 123,
        fornavn = fornavn,
        etternavn = etternavn,
        telefonnummer = telefonnummer,
        email = "",
        postnummer = Postnummer("1234", "Lillevik", Kommune("", "", 0, 0)),
        fylke = 0,
        lokallag = 1,
        groupID = 0,
        kilde = Kilde.Hypersys,
        sistOppdatert = null,
    ).also { it.id = 10 }
}
