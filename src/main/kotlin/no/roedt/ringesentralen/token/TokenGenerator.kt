package no.roedt.ringesentralen.token

import io.smallrye.jwt.build.Jwt
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.time.Duration
import javax.enterprise.context.RequestScoped

@RequestScoped
class TokenGenerator {

    @ConfigProperty(name = "frontendTokenKey")
    lateinit var frontendTokenKey: String

    fun generateToken(key: String): String {
        if (key != frontendTokenKey) {
            throw IllegalArgumentException("Illegal key")
        }
        return generateToken()
    }

    fun generateToken(): String = Jwt
        .audience("ringar")
        .issuer("https://ringesentralen.no")
        .subject("Ringesentralen")
        .upn("Ringesentralen")
        .issuedAt(System.currentTimeMillis())
        .expiresAt(System.currentTimeMillis() + Duration.ofMinutes(1).toSeconds())
        .groups("ringar")
        .sign("META-INF/resources/privatekey.pem")
}