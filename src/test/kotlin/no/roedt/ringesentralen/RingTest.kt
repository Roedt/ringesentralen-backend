package no.roedt.ringesentralen

import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.persistence.EntityManager
import javax.persistence.TypedQuery

internal class RingTest {

    val entityManager: EntityManager = mock()
    val personRepository: PersonRepository = mock()

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
        doReturn(listOf(1234L)).whenever(typedQuery).resultList
        doReturn(typedQuery).whenever(entityManager).createNamedQuery(any(), eq(Long::class.javaObjectType))

        ringController.hentNestePersonAaRinge()
        verify(entityManager).createNamedQuery(any(), eq(Long::class.javaObjectType))
        verify(personRepository).findById(1234)
    }

    @Test
    fun `returnerer utan svar viss ingen fleire aa ringe no`() {
        val typedQuery: TypedQuery<Long> = mock()
        val emptyList: List<Long> = listOf()
        doReturn(emptyList).whenever(typedQuery).resultList
        doReturn(typedQuery).whenever(entityManager).createNamedQuery(any(), eq(Long::class.javaObjectType))

        ringController.hentNestePersonAaRinge()
        verify(entityManager).createNamedQuery(any(), eq(Long::class.javaObjectType))
        verifyZeroInteractions(personRepository)
    }

}