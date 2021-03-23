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

class AESUtil(val secretFactory: SecretFactory) {

    private val algorithm: String = "AES/CBC/PKCS5Padding"
    private val charset = Charsets.UTF_8
    lateinit var key: SecretKey

    @PostConstruct
    fun setKey() {
        key = SecretKeySpec(MessageDigest.getInstance("SHA-256").digest(secretFactory.getEncryptionKey().toByteArray(
            charset
        )), "AES")
    }

    fun encrypt(input: String): String {
        val cipher = Cipher.getInstance(algorithm)
        val iv = generateIv()
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        val cipherText = cipher.doFinal(input.toByteArray(charset))
        val encoder = Base64.getEncoder()
        return encoder.encodeToString(iv.iv)  +":" + encoder.encodeToString(cipherText)
    }

    fun decrypt(cipherText: String): String {
        val splitted = cipherText.split(":")
        val decoder = Base64.getDecoder()

        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(decoder.decode(splitted[0])))
        val plainText = cipher.doFinal(decoder.decode(splitted[1]))
        return String(plainText)
    }

    private fun generateIv(): IvParameterSpec {
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        return IvParameterSpec(iv)
    }
}