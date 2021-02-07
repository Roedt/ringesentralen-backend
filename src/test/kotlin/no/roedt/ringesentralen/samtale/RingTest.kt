package no.roedt.ringesentralen.samtale

import com.nhaarman.mockitokotlin2.*
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.PersonRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.ws.rs.core.SecurityContext

internal class RingTest {

    private val entityManager: EntityManager = mock()
    private val personRepository: PersonRepository = mock()
    private val databaseUpdater: DatabaseUpdater = mock()

    lateinit var ringService: RingServiceBean
    lateinit var ringController: RingController

    @BeforeEach
    fun setup() {
        ringService = spy(RingServiceBean(personRepository = personRepository, entityManager = entityManager, databaseUpdater = databaseUpdater))
        ringController = RingController(ringService)
        ringController.jwt = mock()
    }

    @Test
    fun `hentar neste person aa ringe`() {
        val typedQuery: TypedQuery<Long> = mock()
        doReturn(listOf(1234)).whenever(typedQuery).resultList
        doReturn(typedQuery).whenever(entityManager).createNativeQuery(any())

        ringController.hentNestePersonAaRinge(getContext())
        verify(entityManager).createNativeQuery(any())
        verify(personRepository).findById(1234)
    }

    @Test
    fun `returnerer utan svar viss ingen fleire aa ringe no`() {
        val typedQuery: TypedQuery<Long> = mock()
        val emptyList: List<Long> = listOf()
        doReturn(emptyList).whenever(typedQuery).resultList
        doReturn(typedQuery).whenever(entityManager).createNativeQuery(any())


        ringController.hentNestePersonAaRinge(getContext())
        verify(entityManager).createNativeQuery(any())
        verifyZeroInteractions(personRepository)
    }

    private fun getContext(): SecurityContext {
        val ctx : SecurityContext = mock()
        doReturn(ringController.jwt).whenever(ctx).userPrincipal
        doReturn(Optional.of(1)).whenever(ringController.jwt).claim<Any>(any())
        doReturn(1).whenever(ringService).getLokallag(any())
        return ctx
    }

}