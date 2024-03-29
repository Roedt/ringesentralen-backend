package no.roedt.ringesentralen.samtale.start

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import no.roedt.Kilde
import no.roedt.kommune.Kommune
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
    private val personService: PersonService = mock()
    private val nesteAaRingeRepository: NesteAaRingeRepository = mock()
    private val oppfoelgingValg21Service: OppfoelgingValg21Service = mock()
    private val lokallagService: LokallagService = mock()
    private val oppslagService: OppslagService = mock()
    private val nesteMedlemAaRingeFinder: NesteMedlemAaRingeFinder = mock()

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
        doReturn(lagPerson("Peder", "Ås", "123")).whenever(personService).getPerson(any())
    }

    @Test
    fun `hentar neste person aa ringe`() {
        doReturn(listOf(1234)).whenever(nesteAaRingeRepository).hentNesteIkkemedlem(any(), any())

        nestePersonAaRingeFinder.hentNestePersonAaRinge(
            AutentisertNestePersonAaRingeRequest(
                userId = UserId(userId = 1),
                modus = Modus.velgere,
                lokallag = 1,
                roller = setOf()
            )
        )

        verify(nesteAaRingeRepository).hentNesteIkkemedlem(any(), any())
        verify(personService).findById(1234)
    }

    @Test
    fun `returnerer utan svar viss ingen fleire aa ringe no`() {
        val emptyList: List<Long> = listOf()
        doReturn(emptyList).whenever(nesteAaRingeRepository).hentNesteIkkemedlem(any(), any())
        verifyNoMoreInteractions(personService)

        nestePersonAaRingeFinder.hentNestePersonAaRinge(
            AutentisertNestePersonAaRingeRequest(
                userId = UserId(userId = 1),
                modus = Modus.velgere,
                lokallag = 1,
                roller = setOf()
            )
        )
        verify(nesteAaRingeRepository).hentNesteIkkemedlem(any(), any())
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
        sistOppdatert = null
    )
}
