package no.roedt.token.fake

import io.smallrye.jwt.build.Jwt
import no.roedt.brukere.GenerellRolle
import no.roedt.brukere.mfa.MFARequest
import no.roedt.forum.ForumRolle
import no.roedt.hypersys.login.LoginRequest
import no.roedt.person.UserId
import no.roedt.ringesentralen.RingespesifikkRolle
import no.roedt.token.PrivateKeyFactory
import no.roedt.token.SecretFactory
import no.roedt.token.TokenService
import java.time.Duration

class FakeTokenService(
    private val secretFactory: SecretFactory,
    private val privateKeyFactory: PrivateKeyFactory,
    private val tokenExpiryPeriod: Duration
) : TokenService {
    override fun login(loginRequest: LoginRequest): String {
        if (loginRequest.key != secretFactory.getFrontendTokenKey()) {
            println("Illegal key")
        }

        return Jwt
            .audience("ringer")
            .issuer("https://ringesentralen.no")
            .subject("FakeRingesentralen")
            .upn("FakeRingesentralen")
            .issuedAt(System.currentTimeMillis())
            .expiresAt(System.currentTimeMillis() + tokenExpiryPeriod.toSeconds())
            .groups(setOf(GenerellRolle.BRUKER, ForumRolle.DEBATTANT, RingespesifikkRolle.RINGER_MEDLEMMER))
            .claim("hypersys.user_id", "15424") // Donald
            .sign(privateKeyFactory.readPrivateKey())
    }

    override fun trengerMFA(mfaRequest: MFARequest) = false

    override fun sendMFA(mfaRequest: MFARequest) {}

    override fun hentRoller(userId: UserId): Set<String> = setOf()
}
