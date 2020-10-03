package no.roedt.ringesentralen

import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.persistence.EntityManager
import javax.persistence.TypedQuery

internal class RingTest {

    private val entityManager: EntityManager = mock()
    private val personRepository: PersonRepository = mock()

    lateinit var ringService: RingService
    lateinit var ringController: RingController

    @BeforeEach
    fun setup() {
        ringService = RingServiceBean(personRepository = personRepository, entityManager = entityManager)
        ringController = RingController(ringService)
    }

    @Test
    fun `hentar neste person aa ringe`() {
        val typedQuery: TypedQuery<Long> = mock()
        doReturn(listOf(1234)).whenever(typedQuery).resultList
        doReturn(typedQuery).whenever(entityManager).createNativeQuery(any())

        ringController.hentNestePersonAaRinge(1)
        verify(entityManager).createNativeQuery(any())
        verify(personRepository).findById(1234)
    }

    @Test
    fun `returnerer utan svar viss ingen fleire aa ringe no`() {
        val typedQuery: TypedQuery<Long> = mock()
        val emptyList: List<Long> = listOf()
        doReturn(emptyList).whenever(typedQuery).resultList
        doReturn(typedQuery).whenever(entityManager).createNativeQuery(any())

        ringController.hentNestePersonAaRinge(1)
        verify(entityManager).createNativeQuery(any())
        verifyZeroInteractions(personRepository)
    }

}