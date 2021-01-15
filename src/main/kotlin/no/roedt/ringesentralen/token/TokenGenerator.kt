package no.roedt.ringesentralen.token

import io.smallrye.jwt.build.Jwt
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration
import javax.annotation.PostConstruct
import javax.enterprise.context.RequestScoped

@RequestScoped
class TokenGenerator {
    private var key: String? = null

    @PostConstruct
    fun init() {
        key = Files.readString(Paths.get("../src/main/resources/META-INF/resources/privatekey.pem"))
    }

    fun generateToken(): String = Jwt
        .audience("ringar")
        .issuer("https://ringesentralen.no")
        .subject("Ringesentralen")
        .upn("Ringesentralen")
        .issuedAt(System.currentTimeMillis())
        .expiresAt(System.currentTimeMillis() + Duration.ofMinutes(30).toSeconds())
        .groups("ringar")
        .sign("META-INF/resources/privatekey.pem")
}