package no.roedt.hypersys.konvertering

import io.mockk.mockk
import no.roedt.lokallag.LokallagService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class LokallagConverterTest {
    private val lokallagService: LokallagService = mockk()

    private val lokallagConverter = LokallagConverter(lokallagService)

    @Test
    fun `taklar manglande lokallag`() {
        assertEquals(actual = -1, expected = lokallagConverter.tilLokallag(listOf()))
    }
}
