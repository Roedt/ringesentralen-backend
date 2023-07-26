package no.roedt.ringesentralen.samtale.start

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import no.roedt.DatabaseUpdater
import no.roedt.Kilde
import no.roedt.Kommune
import no.roedt.lokallag.LokallagService
import no.roedt.person.Person
import no.roedt.person.PersonRepository
import no.roedt.person.Postnummer
import no.roedt.person.UserId
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.samtale.OppfoelgingValg21Repository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NestePersonAaRingeFinderTest {

    private val personRepository: PersonRepository = mock()
    private val databaseUpdater: DatabaseUpdater = mock()
    private val oppfoelgingValg21Repository: OppfoelgingValg21Repository = mock()
    private val lokallagService: LokallagService = mock()
    private val oppslagRepository: OppslagRepository = mock()
    private val nesteMedlemAaRingeFinder: NesteMedlemAaRingeFinder = mock()

    private val nestePersonAaRingeFinder = NestePersonAaRingeFinder(
        personRepository = personRepository,
        databaseUpdater = databaseUpdater,
        oppslagRepository = oppslagRepository,
        oppfoelgingValg21Repository = oppfoelgingValg21Repository,
        nesteMedlemAaRingeFinder = nesteMedlemAaRingeFinder,
        lokallagService = lokallagService,
        nyligeOppslagCache = NyligeOppslagCache()
    )

    @BeforeEach
    fun setup() {
        doReturn(lagPerson("Peder", "Ås", "123")).whenever(personRepository).getPerson(any())
    }

    @Test
    fun `hentar neste person aa ringe`() {
        doReturn(listOf(1234)).whenever(databaseUpdater).getResultList(any())

        nestePersonAaRingeFinder.hentNestePersonAaRinge(
            AutentisertNestePersonAaRingeRequest(
                userId = UserId(userId = 1),
                modus = Modus.velgere,
                lokallag = 1,
                roller = setOf()
            )
        )

        verify(databaseUpdater).getResultList(any())
        verify(personRepository).findById(1234)
    }

    @Test
    fun `returnerer utan svar viss ingen fleire aa ringe no`() {
        val emptyList: List<Long> = listOf()
        doReturn(emptyList).whenever(databaseUpdater).getResultList(any())
        verifyNoMoreInteractions(personRepository)

        nestePersonAaRingeFinder.hentNestePersonAaRinge(
            AutentisertNestePersonAaRingeRequest(
                userId = UserId(userId = 1),
                modus = Modus.velgere,
                lokallag = 1,
                roller = setOf()
            )
        )
        verify(databaseUpdater).getResultList(any())
    }

    private fun lagPerson(fornavn: String, etternavn: String, telefonnummer: String) = Person(
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
