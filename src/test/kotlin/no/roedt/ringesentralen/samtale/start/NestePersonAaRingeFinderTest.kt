package no.roedt.ringesentralen.samtale.start

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import no.roedt.lokallag.LokallagRepository
import no.roedt.person.Person
import no.roedt.person.PersonRepository
import no.roedt.person.UserId
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.samtale.OppfoelgingValg21Repository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NestePersonAaRingeFinderTest {

    private val personRepository: PersonRepository = mock()
    private val databaseUpdater: DatabaseUpdater = mock()
    private val oppfoelgingValg21Repository: OppfoelgingValg21Repository = mock()
    private val lokallagRepository: LokallagRepository = mock()
    private val oppslagRepository: OppslagRepository = mock()
    private val nesteMedlemAaRingeFinder: NesteMedlemAaRingeFinder = mock()

    private val nestePersonAaRingeFinder = NestePersonAaRingeFinder(
        personRepository = personRepository,
        databaseUpdater = databaseUpdater,
        oppslagRepository = oppslagRepository,
        oppfoelgingValg21Repository = oppfoelgingValg21Repository,
        nesteMedlemAaRingeFinder = nesteMedlemAaRingeFinder,
        lokallagRepository = lokallagRepository,
        nyligeOppslagCache = NyligeOppslagCache()
    )

    @BeforeEach
    fun setup() {
        doReturn(Person()).whenever(personRepository).getPerson(any())
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
}
