package no.roedt.token

import io.smallrye.jwt.build.Jwt
import no.roedt.hypersys.login.AESUtil
import no.roedt.hypersys.login.LoginRequest
import no.roedt.ringesentralen.Roles
import no.roedt.ringesentralen.person.EpostValidator
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.time.Duration
import javax.enterprise.context.RequestScoped

@RequestScoped
class FakeTokenService(
    private val secretFactory: SecretFactory,
    private val privateKeyFactory: PrivateKeyFactory,
    private val aesUtil: AESUtil
) {

    @ConfigProperty(name = "token.expiryPeriod")
    lateinit var tokenExpiryPeriod: Duration

    fun login(loginRequest: LoginRequest): String {
        if (loginRequest.key != secretFactory.getFrontendTokenKey()) {
            throw IllegalArgumentException("Illegal key")
        }
        aesUtil.decrypt(loginRequest.brukarnamn).also { EpostValidator.validate(it) }

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
