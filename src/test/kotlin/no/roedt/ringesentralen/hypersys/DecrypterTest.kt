package no.roedt.ringesentralen.hypersys

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import no.roedt.ringesentralen.token.SecretFactory
import org.junit.jupiter.api.Test
import java.util.*
import javax.crypto.Cipher
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


internal class DecrypterTest {

    @Test
    fun dekryptertStringBlirDenOriginale() {
        val secretFactory : SecretFactory = mock()
        val secretKey = "AvantiPopoloAllaRiscossa"
        doReturn(secretKey).whenever(secretFactory).getEncryptionKey()
        val decrypter = Decrypter(secretFactory)

        val originalString = "mads.opheim@gmail.com"
        val encryptedString: String = encrypt(decrypter, originalString, secretKey)
        val decryptedString: String = decrypter.decrypt(encryptedString)

        assertNotEquals(encryptedString, originalString)
        assertEquals(expected = originalString, actual = decryptedString)
    }

    private fun encrypt(decrypter: Decrypter, strToEncrypt: String, secret: String): String {
        val secretKey = decrypter.createKey(secret)
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.toByteArray(Charsets.UTF_8)))
    }
}