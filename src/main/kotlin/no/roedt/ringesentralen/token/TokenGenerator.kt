package no.roedt.ringesentralen.token

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretVersionName
import io.smallrye.jwt.build.Jwt
import no.roedt.ringesentralen.hypersys.*
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.nio.file.Files
import java.nio.file.Path
import java.time.Duration
import javax.enterprise.context.RequestScoped

@RequestScoped
class TokenGenerator(val hypersysService: HypersysService) {

    @ConfigProperty(name = "frontendTokenKey")
    lateinit var frontendTokenKey: String

    @ConfigProperty(name = "usePrivateKeyFromSecretManager", defaultValue = "false")
    lateinit var usePrivateKeyFromSecretManager: String

    @ConfigProperty(name = "secretManagerProjectId", defaultValue = "")
    lateinit var secretManagerProjectId: String

    @ConfigProperty(name = "secretManagerSecretName", defaultValue = "")
    lateinit var secretManagerSecretName: String

    fun login(loginRequest: LoginRequest): String {
        if (loginRequest.key != frontendTokenKey) {
            throw IllegalArgumentException("Illegal key")
        }

        val hypersysToken: Token = hypersysService.login(loginRequest)
        if (hypersysToken is UgyldigToken)
            throw RuntimeException(hypersysToken.error)
        return generateToken(hypersysToken as GyldigToken)
    }

    private fun generateToken(hypersysToken: GyldigToken): String = Jwt
        .audience("ringar")
        .issuer("https://ringesentralen.no")
        .subject("Ringesentralen")
        .upn("Ringesentralen")
        .issuedAt(System.currentTimeMillis())
        .expiresAt(System.currentTimeMillis() + Duration.ofMinutes(1).toSeconds())
        .groups("ringar")
        .claim("hypersys.token_type", hypersysToken.token_type)
        .claim("hypersys.scope", hypersysToken.scope)
        .claim("hypersys.access_token", hypersysToken.access_token)
        .claim("hypersys.expires_in", hypersysToken.expires_in)
        .signWithSecret(getPrivateKey())

    private fun getPrivateKey(): String =
        if (usePrivateKeyFromSecretManager.toBoolean()) getPrivateKeyFromSecretManager()
        else Files.readString(Path.of("../src/main/resources/META-INF/resources/privatekey.pem"))

    private fun getPrivateKeyFromSecretManager(): String {
        val client = SecretManagerServiceClient.create()
        val secretVersionName = SecretVersionName.of(secretManagerProjectId, secretManagerSecretName, "latest")
        return client.accessSecretVersion(secretVersionName).payload.data.toStringUtf8()
    }
}
