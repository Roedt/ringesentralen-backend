package no.roedt.hypersys

import com.nhaarman.mockitokotlin2.mock
import no.roedt.lokallag.LokallagRepository
import org.junit.jupiter.api.Test

internal class LokallagConverterTest {

    private val lokallagRepository: LokallagRepository = mock()

    private val lokallagConverter = LokallagConverter(lokallagRepository)

    @Test
    fun `taklar manglande lokallag`() {
        kotlin.test.assertEquals(actual = -1, expected = lokallagConverter.tilLokallag(listOf()))
    }
}