package no.roedt.ringesentralen.samtale

import com.nhaarman.mockitokotlin2.*
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.hypersys.RingerRepository
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import javax.ws.rs.core.SecurityContext

internal class RingTest {

    private val personRepository: PersonRepository = mock()
    private val databaseUpdater: DatabaseUpdater = mock()
    private val oppslagRepository: OppslagRepository = mock()
    private val persistentSamtaleRepository: PersistentSamtaleRepository = mock()
    private val oppfoelgingValg21Repository: OppfoelgingValg21Repository = mock()
    private val nesteMedlemAaRingeFinder: NesteMedlemAaRingeFinder = mock()
    private val lokallagRepository: LokallagRepository = mock()
    private val ringerRepository: RingerRepository = mock()


    lateinit var ringService: RingServiceBean
    lateinit var ringController: RingController

    @BeforeEach
    fun setup() {
        ringService = spy(RingServiceBean(personRepository = personRepository, databaseUpdater = databaseUpdater, oppslagRepository = oppslagRepository,
            samtaleRepository = persistentSamtaleRepository, oppfoelgingValg21Repository = oppfoelgingValg21Repository, nesteMedlemAaRingeFinder = nesteMedlemAaRingeFinder,
            lokallagRepository = lokallagRepository, ringerRepository = ringerRepository))
        ringController = RingController(ringService)
        ringController.jwt = mock()
    }

    @Test
    fun `hentar neste person aa ringe`() {

        doReturn(listOf(1234)).whenever(databaseUpdater).getResultList(any())

        ringController.hentNestePersonAaRinge(getContext(), Modus.velgere)
        verify(databaseUpdater).getResultList(any())
        verify(personRepository).findById(1234)
    }

    @Test
    fun `returnerer utan svar viss ingen fleire aa ringe no`() {
        val emptyList: List<Long> = listOf()
        doReturn(emptyList).whenever(databaseUpdater).getResultList(any())

        ringController.hentNestePersonAaRinge(getContext(), Modus.velgere)
        verify(databaseUpdater).getResultList(any())
        verifyZeroInteractions(personRepository)
    }

    private fun getContext(): SecurityContext {
        val ctx : SecurityContext = mock()
        doReturn(ringController.jwt).whenever(ctx).userPrincipal
        doReturn(Optional.of(1)).whenever(ringController.jwt).claim<Any>(any())
        doReturn(Person()).whenever(ringService).getPerson(any())
        return ctx
    }

}