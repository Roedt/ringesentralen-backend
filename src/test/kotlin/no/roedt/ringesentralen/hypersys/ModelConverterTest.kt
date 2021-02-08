package no.roedt.ringesentralen.hypersys

import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Test
import javax.persistence.EntityManager
import kotlin.test.assertNull

internal class ModelConverterTest {

    private val entityManager: EntityManager = mock()

    private val modelConverter: ModelConverterBean = ModelConverterBean(
        entityManager = entityManager
    )

    @Test
    fun `taklar manglande lokallag`() {
        assertNull(modelConverter.toLokallag(listOf()))
    }
}