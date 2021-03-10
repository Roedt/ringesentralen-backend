package no.roedt.ringesentralen.token

import io.smallrye.jwt.build.Jwt
import no.roedt.ringesentralen.Roles
import no.roedt.ringesentralen.hypersys.LoginRequest
import no.roedt.ringesentralen.person.EpostValidator
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.time.Duration
import javax.enterprise.context.RequestScoped

@RequestScoped
class FakeTokenService(private val gcpSecretManager: GCPSecretManager,
                       private val privateKeyFactory: PrivateKeyFactory) {

    @ConfigProperty(name = "token.expiryPeriod")
    lateinit var tokenExpiryPeriod: Duration

    fun login(loginRequest: LoginRequest): String {
        if (loginRequest.key != gcpSecretManager.getFrontendTokenKey()) {
            throw IllegalArgumentException("Illegal key")
        }
        EpostValidator.validate(loginRequest.brukarnamn)

        return Jwt
            .audience("ringer")
            .issuer("https://ringesentralen.no")
            .subject("FakeRingesentralen")
            .upn("FakeRingesentralen")
            .issuedAt(System.currentTimeMillis())
            .expiresAt(System.currentTimeMillis() + tokenExpiryPeriod.toSeconds())
            .groups(setOf(Roles.bruker))
            .claim("hypersys.user_id", "15424") // Donald
            .sign(privateKeyFactory.readPrivateKey())
    }
}