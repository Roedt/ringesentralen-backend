package no.roedt.ringesentralen.token

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretVersionName
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.annotation.PostConstruct
import javax.enterprise.context.RequestScoped

interface SecretFactory {
    fun getPrivateKey(): String
    fun getFrontendTokenKey(): String
    fun getHypersysBrukerId(): String
    fun getHypersysBrukerSecret(): String
    fun getHypersysClientId(): String
    fun getHypersysClientSecret(): String
    fun getHypersysBaseURL(): String
}

@RequestScoped
class GCPSecretManager : SecretFactory {

    @ConfigProperty(name = "secretManagerProjectId", defaultValue = "")
    lateinit var secretManagerProjectId: String

    override fun getPrivateKey() = getSecretFromSecretManager("privatekey")

    lateinit var client: SecretManagerServiceClient

    @PostConstruct
    fun setup() {
        client = SecretManagerServiceClient.create()
    }

    override fun getFrontendTokenKey() = getSecretFromSecretManager("frontendTokenKey")

    override fun getHypersysBrukerId() = getSecretFromSecretManager("hypersysBrukerId")

    override fun getHypersysBrukerSecret() = getSecretFromSecretManager("hypersysBrukerSecret")

    override fun getHypersysClientId() = getSecretFromSecretManager("hypersysClientId")

    override fun getHypersysClientSecret() = getSecretFromSecretManager("hypersysClientSecret")

    override fun getHypersysBaseURL() = getSecretFromSecretManager("hypersysBaseUrl")

    private fun getSecretFromSecretManager(secretName: String): String {
        val secretVersionName = SecretVersionName.of(secretManagerProjectId, secretName, "latest")
        return client.accessSecretVersion(secretVersionName).payload.data.toStringUtf8()
    }
}