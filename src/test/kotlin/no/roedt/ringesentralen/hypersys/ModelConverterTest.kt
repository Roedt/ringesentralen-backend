package no.roedt.ringesentralen.hypersys

import com.nhaarman.mockitokotlin2.mock
import no.roedt.ringesentralen.lokallag.LokallagRepository
import org.junit.jupiter.api.Test
import javax.persistence.EntityManager
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class ModelConverterTest {

    private val entityManager: EntityManager = mock()
    private val lokallagRepository: LokallagRepository = mock()

    private val modelConverter: ModelConverterBean = ModelConverterBean(
        entityManager = entityManager,
        lokallagRepository = lokallagRepository
    )

    @Test
    fun `taklar manglande telefonnummer`() {
        assertNull(modelConverter.toTelefonnummer(""))
    }

    @Test
    fun `parsar vanleg telefonnumer ok`() {
        assertEquals(actual = modelConverter.toTelefonnummer("+47 81549300"), expected = "81549300")
    }

    @Test
    fun `taklar manglande lokallag`() {
        assertNull(modelConverter.toLokallag(listOf()))
    }
}