package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.token.SecretFactory
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*
import javax.annotation.PostConstruct
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.enterprise.context.Dependent

@Dependent
class AESUtil(val secretFactory: SecretFactory) {

    private val algorithm: String = "AES/CBC/PKCS5Padding"
    private val decoder = Base64.getDecoder()
    lateinit var key: SecretKey

    @PostConstruct
    fun setKey() {
        key = SecretKeySpec(MessageDigest.getInstance("SHA-256").digest(secretFactory.getEncryptionKey().encodeToByteArray()), "AES")
    }

    fun encrypt(input: String): String {
        val iv = IvParameterSpec(ByteArray(16).also { SecureRandom().nextBytes(it) })
        val encoder = Base64.getEncoder()

        return Cipher.getInstance(algorithm)
            .also { it.init(Cipher.ENCRYPT_MODE, key, iv) }
            .doFinal(input.encodeToByteArray())
            .let { encoder.encodeToString(iv.iv)  +":" + encoder.encodeToString(it) }
    }

    fun decrypt(cipherText: String): String {
        val splitted = cipherText.split(":")
        val ivString = splitted[0]
        val secretString = splitted[1]

        val iv = IvParameterSpec(decoder.decode(ivString))

        return Cipher.getInstance(algorithm)
            .also { it.init(Cipher.DECRYPT_MODE, key, iv) }
            .doFinal(decoder.decode(secretString))
            .let { String(it) }
    }
}