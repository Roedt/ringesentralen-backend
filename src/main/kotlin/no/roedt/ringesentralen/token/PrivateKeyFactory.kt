package no.roedt.ringesentralen.token

import org.eclipse.microprofile.config.inject.ConfigProperty
import java.nio.file.Files
import java.nio.file.Path
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import javax.enterprise.context.RequestScoped

@RequestScoped
class PrivateKeyFactory(private val gcpSecretManager: GCPSecretManager) {

    @ConfigProperty(name = "usePrivateKeyFromSecretManager", defaultValue = "false")
    lateinit var usePrivateKeyFromSecretManager: String

    fun readPrivateKey(): RSAPrivateKey = readPrivateKey(getPrivateKey())

    private fun getPrivateKey(): String =
        if (usePrivateKeyFromSecretManager.toBoolean()) gcpSecretManager.getPrivateKeyFromSecretManager()
        else Files.readString(Path.of("../src/main/resources/META-INF/resources/privatekey.pem"))

    private fun readPrivateKey(key: String): RSAPrivateKey {
        val privateKeyPEM = key
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace(System.lineSeparator().toRegex(), "")
            .replace("-----END PRIVATE KEY-----", "")
        val encoded: ByteArray = Base64.getDecoder().decode(privateKeyPEM)
        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec = PKCS8EncodedKeySpec(encoded)
        return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
    }
}