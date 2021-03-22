package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.token.SecretFactory
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.enterprise.context.Dependent


@Dependent
class Decrypter(val secretFactory: SecretFactory) {

    fun createKey(myKey: String) = SecretKeySpec(MessageDigest.getInstance("SHA-1").digest(myKey.toByteArray(Charsets.UTF_8)).copyOf(16), "AES")

    fun decrypt(strToDecrypt: String): String {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, createKey(secretFactory.getEncryptionKey()))
        return String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)))
    }
}