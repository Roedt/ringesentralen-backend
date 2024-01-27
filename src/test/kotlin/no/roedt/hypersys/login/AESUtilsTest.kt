package no.roedt.hypersys.login

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import no.roedt.token.SecretFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

internal class AESUtilsTest {
    private val secretFactory: SecretFactory = mock()

    @Test
    fun `kan kryptere og saa dekryptere og faa den teksten vi starta med`() {
        val encryptionKey = "FfZdV0Cu13lscPPVjYjJGRZmp0afpY3h"
        doReturn(encryptionKey).whenever(secretFactory).getEncryptionKey()

        val input = "her er min veldig kryptiske tekst"

        val aesUtil = AESUtil(secretFactory).also { it.setKey() }

        val cipherText =
            encrypt(input = input, algorithm = aesUtil.algorithm, key = SecretKeySpec(encryptionKey.encodeToByteArray(), "AES"))
        val plainText = aesUtil.decrypt(cipherText)

        Assertions.assertEquals(input, plainText)
    }

    private fun encrypt(
        input: String,
        algorithm: String,
        key: SecretKey
    ): String {
        val iv = IvParameterSpec(ByteArray(16).also { SecureRandom().nextBytes(it) })
        val encoder = Base64.getEncoder()

        return Cipher.getInstance(algorithm)
            .also { it.init(Cipher.ENCRYPT_MODE, key, iv) }
            .doFinal(input.encodeToByteArray())
            .let { encoder.encodeToString(iv.iv) + ":" + encoder.encodeToString(it) }
    }
}
