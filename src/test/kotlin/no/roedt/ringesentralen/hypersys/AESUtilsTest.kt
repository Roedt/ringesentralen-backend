package no.roedt.ringesentralen.hypersys

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import no.roedt.ringesentralen.token.SecretFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class AESUtilUnitTest {

    private val secretFactory: SecretFactory = mock()

    @Test
    fun `kan kryptere og saa dekryptere og faa den teksten vi starta med`() {
        doReturn("FfZdV0Cu13lscPPVjYjJGRZmp0afpY3h").whenever(secretFactory).getEncryptionKey()

        val input = "her er min veldig kryptiske tekst"

        val aesUtil = AESUtil(secretFactory).also { it.setKey() }

        val cipherText = aesUtil.encrypt(input)
        val plainText = aesUtil.decrypt(cipherText)

        Assertions.assertEquals(input, plainText)
    }
}