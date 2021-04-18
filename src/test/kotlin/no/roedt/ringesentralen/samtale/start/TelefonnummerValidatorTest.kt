package no.roedt.ringesentralen.samtale.start

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import javax.ws.rs.BadRequestException

internal class TelefonnummerValidatorTest {

    @Test
    fun `norske mobilnummer paa 9 er gyldige`() {
        assertDoesNotThrow { TelefonnummerValidator.validate("+4799999999") }
    }

    @Test
    fun `norske mobilnummer paa 4 er gyldige`() {
        assertDoesNotThrow { TelefonnummerValidator.validate("+4749999999") }
    }

    @Test
    fun `norske fasttelefonnummer paa 7 er gyldige`() {
        assertDoesNotThrow { TelefonnummerValidator.validate("+4779999999") }
    }

    @Test
    fun `norske mobilnummer uten +47 er ugyldige`() {
        assertThrows<BadRequestException> { TelefonnummerValidator.validate("99999999") }
    }

    @Test
    fun `berre tullball er ugyldig`() {
        assertThrows<BadRequestException> { TelefonnummerValidator.validate("berre tullball") }
    }
}