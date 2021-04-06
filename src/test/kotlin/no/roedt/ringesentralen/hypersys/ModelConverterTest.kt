package no.roedt.ringesentralen.hypersys

import com.nhaarman.mockitokotlin2.mock
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.KommuneRepository
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.PersonRepository
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class ModelConverterTest {

    private val databaseUpdater: DatabaseUpdater = mock()
    private val lokallagRepository: LokallagRepository = mock()
    private val personRepository: PersonRepository = mock()
    private val kommuneRepository: KommuneRepository = mock()

    private val modelConverter: ModelConverterBean = ModelConverterBean(
        databaseUpdater = databaseUpdater,
        lokallagRepository = lokallagRepository,
        personRepository = personRepository,
        kommuneRepository = kommuneRepository
    )

    @Test
    fun `taklar manglande telefonnummer`() {
        assertNull(modelConverter.toTelefonnummer(""))
    }

    @Test
    fun `parsar vanleg telefonnumer ok`() {
        assertEquals(actual = modelConverter.toTelefonnummer("+47 81549300"), expected = "+4781549300")
    }

    @Test
    fun `taklar manglande lokallag`() {
        assertEquals(actual = -1, expected = modelConverter.toLokallag(listOf()))
    }
}