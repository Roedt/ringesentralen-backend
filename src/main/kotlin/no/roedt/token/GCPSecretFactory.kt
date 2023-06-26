package no.roedt.token

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretVersionName
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.eclipse.microprofile.config.inject.ConfigProperty

private enum class GCPSecretManagerKey {
    privatekey,
    frontendTokenKey,
    hypersysBrukerId,
    hypersysBrukerSecret,
    hypersysClientId,
    hypersysClientSecret,
    frontendSystembruker,
    frontendSystembrukerPassord,
    encryptionKey
}

class GCPSecretFactory(
    @ConfigProperty(name = "secretManagerProjectId", defaultValue = "")
    val secretManagerProjectId: String
) : SecretFactory {
    private lateinit var client: SecretManagerServiceClient

    @jakarta.annotation.PostConstruct
    fun setup() {
        client = SecretManagerServiceClient.create()
    }

    @PreDestroy
    fun tearDown() {
        client.close()
    }

    override fun getPrivateKey() = getSecretFromSecretManager(GCPSecretManagerKey.privatekey)

    override fun getFrontendTokenKey() = getSecretFromSecretManager(GCPSecretManagerKey.frontendTokenKey)

    override fun getHypersysBrukerId() = getSecretFromSecretManager(GCPSecretManagerKey.hypersysBrukerId)

    override fun getHypersysBrukerSecret() = getSecretFromSecretManager(GCPSecretManagerKey.hypersysBrukerSecret)

    override fun getHypersysClientId() = getSecretFromSecretManager(GCPSecretManagerKey.hypersysClientId)

    override fun getHypersysClientSecret() = getSecretFromSecretManager(GCPSecretManagerKey.hypersysClientSecret)

    override fun getFrontendSystembruker() = getSecretFromSecretManager(GCPSecretManagerKey.frontendSystembruker)

    override fun getFrontendSystembrukerPassord() =
        getSecretFromSecretManager(GCPSecretManagerKey.frontendSystembrukerPassord)

    override fun getEncryptionKey() = getSecretFromSecretManager(GCPSecretManagerKey.encryptionKey)

    private fun getSecretFromSecretManager(secretName: GCPSecretManagerKey): String {
        val secretVersionName = SecretVersionName.of(secretManagerProjectId, secretName.name, "latest")
        return client.accessSecretVersion(secretVersionName).payload.data.toStringUtf8()
    }
}
