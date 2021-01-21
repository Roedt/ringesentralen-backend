package no.roedt.ringesentralen.token

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretVersionName
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.RequestScoped

@RequestScoped
class GCPSecretManager {

    @ConfigProperty(name = "secretManagerProjectId", defaultValue = "")
    lateinit var secretManagerProjectId: String

    @ConfigProperty(name = "secretManagerSecretName", defaultValue = "")
    lateinit var secretManagerSecretName: String

    internal fun getPrivateKeyFromSecretManager(): String {
        val client = SecretManagerServiceClient.create()
        val secretVersionName = SecretVersionName.of(secretManagerProjectId, secretManagerSecretName, "latest")
        return client.accessSecretVersion(secretVersionName).payload.data.toStringUtf8()
    }
}