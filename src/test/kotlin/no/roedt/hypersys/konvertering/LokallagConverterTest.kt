package no.roedt.hypersys.konvertering

import com.nhaarman.mockitokotlin2.mock
import no.roedt.lokallag.LokallagService
import org.junit.jupiter.api.Test

internal class LokallagConverterTest {
    private val lokallagService: LokallagService = mock()

    private val lokallagConverter = LokallagConverter(lokallagService)

    @Test
    fun `taklar manglande lokallag`() {
        kotlin.test.assertEquals(actual = -1, expected = lokallagConverter.tilLokallag(listOf()))
    }
}
