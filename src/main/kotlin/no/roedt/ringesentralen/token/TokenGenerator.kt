package no.roedt.ringesentralen.token

import io.smallrye.jwt.build.Jwt
import java.time.Duration
import javax.enterprise.context.RequestScoped

@RequestScoped
class TokenGenerator {

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