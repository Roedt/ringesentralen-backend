package no.roedt.ringesentralen.token

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretVersionName
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.RequestScoped

@RequestScoped
class GCPSecretManager {

    @ConfigProperty(name = "secretManagerProjectId", defaultValue = "")
    lateinit var secretManagerProjectId: String

    internal fun getPrivateKeyFromSecretManager() = getSecretFromSecretManager("privatekey")

    fun getHypersysBrukerId() = getSecretFromSecretManager("hypersysBrukerId")

    fun getHypersysBrukerSecret() = getSecretFromSecretManager("hypersysBrukerSecret")

    fun getHypersysClientId() = getSecretFromSecretManager("hypersysClientId")

    fun getHypersysClientSecret() = getSecretFromSecretManager("hypersysClientSecret")

    fun getHypersysBaseURL() = getSecretFromSecretManager("hypersysBaseUrl")

    private fun getSecretFromSecretManager(secretName: String): String {
        val secretVersionName = SecretVersionName.of(secretManagerProjectId, secretName, "latest")
        val client = SecretManagerServiceClient.create()
        return client.accessSecretVersion(secretVersionName).payload.data.toStringUtf8()
    }
}