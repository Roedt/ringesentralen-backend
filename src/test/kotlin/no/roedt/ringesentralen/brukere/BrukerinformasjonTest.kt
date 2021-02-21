package no.roedt.ringesentralen.brukere

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class BrukerinformasjonTest {

    @Test
    fun nulltelefonnummerGirNull() {
        assertNull(toTelefonnummer(null))
    }

    @Test
    fun telefonnummerBlirWrappaIEnkeltfnuttar() {
        assertEquals(actual = toTelefonnummer(Telefonnummer(landkode = "+47", nummer = 12345678)), expected= "'12345678'")
    }

    private fun toTelefonnummer(telefonnummer: Telefonnummer?): String? {
        return Brukerinformasjon(
            id = 0,
            hypersysID = 0,
            fornavn = "",
            etternavn = "",
            epost = "",
            telefonnummer = telefonnummer,
            postnummer = 7021,
            fylke = Fylke(3, "Oslo"),
            lokallag = null,
            rolle = setOf()
        ).toTelefonnummer()
    }
}