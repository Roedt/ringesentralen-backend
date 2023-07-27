package no.roedt.hypersys.konvertering

import com.nhaarman.mockitokotlin2.mock
import no.roedt.brukere.FylkeRepository
import no.roedt.person.PersonService
import no.roedt.person.PostnummerRepository
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class ModelConverterTest {

    private val lokallagConverter: LokallagConverter = LokallagConverter(mock())
    private val personService: PersonService = mock()
    private val fylkeRepository: FylkeRepository = mock()
    private val postnummerRepository: PostnummerRepository = mock()

    private val modelConverter: ModelConverterBean = ModelConverterBean(
        lokallagConverter = lokallagConverter,
        personService = personService,
        fylkeRepository = fylkeRepository,
        postnummerRepository = postnummerRepository
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
