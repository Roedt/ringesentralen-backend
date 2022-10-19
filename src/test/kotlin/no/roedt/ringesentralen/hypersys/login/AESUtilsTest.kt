package no.roedt.ringesentralen.hypersys

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import no.roedt.hypersys.login.AESUtil
import no.roedt.token.SecretFactoryProxy
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

internal class AESUtilsTest {

    private val secretFactory: SecretFactoryProxy = mock()

    @Test
    fun `kan kryptere og saa dekryptere og faa den teksten vi starta med`() {
        doReturn("FfZdV0Cu13lscPPVjYjJGRZmp0afpY3h").whenever(secretFactory).getEncryptionKey()

        val input = "her er min veldig kryptiske tekst"

        val aesUtil = AESUtil(secretFactory).also { it.setKey() }

        val cipherText = encrypt(input = input, algorithm = aesUtil.algorithm, key = aesUtil.key)
        val plainText = aesUtil.decrypt(cipherText)

        Assertions.assertEquals(input, plainText)
    }

    private fun encrypt(input: String, algorithm: String, key: SecretKey): String {
        val iv = IvParameterSpec(ByteArray(16).also { SecureRandom().nextBytes(it) })
        val encoder = Base64.getEncoder()

        return Cipher.getInstance(algorithm)
            .also { it.init(Cipher.ENCRYPT_MODE, key, iv) }
            .doFinal(input.encodeToByteArray())
            .let { encoder.encodeToString(iv.iv) + ":" + encoder.encodeToString(it) }
    }
}
