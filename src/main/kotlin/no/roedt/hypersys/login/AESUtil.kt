package no.roedt.hypersys.login

import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.Dependent
import no.roedt.token.SecretFactory
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Dependent
class AESUtil(val secretFactory: SecretFactory) {
    val algorithm: String = "AES/CBC/PKCS5Padding"
    private lateinit var key: SecretKey

    @PostConstruct
    fun setKey() {
        key = SecretKeySpec(secretFactory.getEncryptionKey().encodeToByteArray(), "AES")
    }

    fun decrypt(cipherText: String): String =
        Cipher.getInstance(algorithm)
            .also { it.init(Cipher.DECRYPT_MODE, key, getIV(cipherText)) }
            .doFinal(decode(cipherText))
            .let { String(it) }

    private fun getIV(cipherText: String) = IvParameterSpec(Base64.getDecoder().decode(cipherText.split(":")[0]))

    private fun decode(cipherText: String) = Base64.getDecoder().decode(cipherText.split(":")[1])
}
