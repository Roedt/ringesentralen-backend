package no.roedt.ringesentralen.hypersys

import com.nhaarman.mockitokotlin2.mock
import no.roedt.brukere.FylkeRepository
import no.roedt.hypersys.LokallagConverter
import no.roedt.hypersys.ModelConverterBean
import no.roedt.person.PersonRepository
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class ModelConverterTest {

    private val lokallagConverter: LokallagConverter = mock()
    private val personRepository: PersonRepository = mock()
    private val fylkeRepository: FylkeRepository = mock()

    private val modelConverter: ModelConverterBean = ModelConverterBean(
        lokallagConverter = lokallagConverter,
        personRepository = personRepository,
        fylkeRepository = fylkeRepository
    )

    @Test
    fun `taklar manglande telefonnummer`() {
        assertNull(modelConverter.toTelefonnummer(""))
    }

    @Test
    fun `parsar vanleg telefonnumer ok`() {
        assertEquals(actual = modelConverter.toTelefonnummer("+47 81549300"), expected = "+4781549300")
    }
}
