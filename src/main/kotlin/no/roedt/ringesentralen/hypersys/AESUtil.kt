package no.roedt.ringesentralen.hypersys

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*
import javax.annotation.PostConstruct
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESUtil {

    private val algorithm: String = "AES/CBC/PKCS5Padding"
    lateinit var key: SecretKey

    @PostConstruct
    fun setKey() {
        val md = MessageDigest.getInstance("SHA-256")
        key = SecretKeySpec(md.digest("Ek6MKdSdEz1EiRs6cX3uUUAAnMYua17h".toByteArray(Charsets.UTF_8)), "AES")
    }

    fun encrypt(input: String, iv: IvParameterSpec?): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        val cipherText = cipher.doFinal(input.toByteArray())
        return Base64.getEncoder().encodeToString(cipherText)
    }

    fun decrypt(cipherText: String, iv: IvParameterSpec?): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, key, iv)
        val plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText))
        return String(plainText)
    }
    fun generateIv(): IvParameterSpec {
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        return IvParameterSpec(iv)
    }

}

//fun getKeyFromPassword(password: String, salt: String): SecretKey {
//    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
//    val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt.toByteArray(), 65536, 256)
//    return SecretKeySpec(
//        factory.generateSecret(spec)
//            .encoded, "AES"
//    )
//}
