package no.roedt.hypersys.konvertering

import com.nhaarman.mockitokotlin2.mock
import no.roedt.fylke.FylkeService
import no.roedt.person.PersonService
import no.roedt.postnummer.PostnummerService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class ModelConverterTest {
    private val lokallagConverter: LokallagConverter = LokallagConverter(mock())
    private val personService: PersonService = mock()
    private val fylkeService: FylkeService = mock()
    private val postnummerService: PostnummerService = mock()

    private val modelConverter: ModelConverterBean =
        ModelConverterBean(
            lokallagConverter = lokallagConverter,
            personService = personService,
            fylkeService = fylkeService,
            postnummerService = postnummerService
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
