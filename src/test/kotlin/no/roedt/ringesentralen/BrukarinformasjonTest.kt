package no.roedt.ringesentralen

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class BrukarinformasjonTest {

    @Test
    fun nulltelefonnummerGirNull() {
        assertNull(toTelefonnummer(null))
    }

    @Test
    fun telefonnummerBlirWrappaIEnkeltfnuttar() {
        assertEquals(actual = toTelefonnummer(Telefonnummer(landkode = "+47", nummer = 12345678)), expected= "'12345678'")
    }

    private fun toTelefonnummer(telefonnummer: Telefonnummer?): String? {
        return Brukarinformasjon(
            hypersysID = 0,
            fornamn = "",
            etternamn = "",
            epost = "",
            telefonnummer = telefonnummer,
            postnummer = Postnummer("7021"),
            fylke = Fylke(3, "Oslo"),
            lokallag = null
        ).toTelefonnummer()
    }
}