package no.roedt.ringesentralen.token

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretVersionName
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.enterprise.context.RequestScoped

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

@RequestScoped
class GCPSecretManager : SecretFactory {

    @ConfigProperty(name = "secretManagerProjectId", defaultValue = "")
    lateinit var secretManagerProjectId: String

    lateinit var client: SecretManagerServiceClient

    @PostConstruct
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

    override fun getFrontendSystembrukerPassord() = getSecretFromSecretManager(GCPSecretManagerKey.frontendSystembrukerPassord)

    override fun getEncryptionKey() = getSecretFromSecretManager(GCPSecretManagerKey.encryptionKey)

    private fun getSecretFromSecretManager(secretName: GCPSecretManagerKey): String {
        val secretVersionName = SecretVersionName.of(secretManagerProjectId, secretName.name, "latest")
        return client.accessSecretVersion(secretVersionName).payload.data.toStringUtf8()
    }
}