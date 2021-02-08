package no.roedt.ringesentralen.token

import io.smallrye.jwt.build.Jwt
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.hypersys.*
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.jwt.JsonWebToken
import java.time.Duration
import javax.enterprise.context.RequestScoped
import javax.json.JsonNumber
import kotlin.math.max


@RequestScoped
class TokenGenerator(
    private val hypersysService: HypersysService,
    private val personRepository: PersonRepository,
    private val privateKeyFactory: PrivateKeyFactory
) {

    @ConfigProperty(name = "frontendTokenKey")
    lateinit var frontendTokenKey: String

    @ConfigProperty(name = "token.expiryPeriod")
    lateinit var tokenExpiryPeriod: Duration

    fun login(loginRequest: LoginRequest): String {
        if (loginRequest.key != frontendTokenKey) {
            throw IllegalArgumentException("Illegal key")
        }

        val hypersysToken: Token = hypersysService.login(loginRequest)
        if (hypersysToken is UgyldigToken)
            throw RuntimeException(hypersysToken.error)
        return generateToken(hypersysToken as GyldigPersonToken)
    }

    private fun generateToken(hypersysToken: GyldigPersonToken): String = Jwt
        .audience("ringar")
        .issuer("https://ringesentralen.no")
        .subject("Ringesentralen")
        .upn("Ringesentralen")
        .issuedAt(System.currentTimeMillis())
        .expiresAt(getTokenExpiresAt())
        .groups(getGroups(hypersysToken))
        .claim("hypersys.token_type", hypersysToken.token_type)
        .claim("hypersys.scope", hypersysToken.scope)
        .claim("hypersys.access_token", hypersysToken.access_token)
        .claim("hypersys.expires_in", hypersysToken.expires_in)
        .claim("hypersys.refresh_token", hypersysToken.refresh_token)
        .claim("hypersys.user_id", hypersysToken.user_id)
        .sign(privateKeyFactory.readPrivateKey())

    private fun getTokenExpiresAt() = System.currentTimeMillis() + tokenExpiryPeriod.toSeconds()

    private fun getGroups(hypersysToken: GyldigPersonToken): Set<String> =
        when (getPersonFromHypersysID(hypersysToken).groupID) {
            GroupID.Admin.nr -> setOf("ringar", "admin", "godkjenner")
            GroupID.LokalGodkjenner.nr -> setOf("ringar", "godkjenner")
            GroupID.GodkjentRinger.nr -> setOf("ringar")
            else -> setOf("uatorisert")
        }

    private fun getPersonFromHypersysID(hypersysToken: GyldigPersonToken) =
        personRepository.find("hypersysID", hypersysToken.user_id.toInt()).firstResult<Person>()

    fun refresh(jwt: JsonWebToken): String = generateToken(
        GyldigPersonToken(
            token_type = extract(jwt, "token_type"),
            scope = extract(jwt, "scope"),
            access_token = extract(jwt, "access_token"),
            expires_in = getExpiresIn(jwt),
            refresh_token = extract(jwt, "refresh_token"),
            user_id = extract(jwt, "user_id")
        )
    )

    private fun getExpiresIn(jwt: JsonWebToken): Int {
        val fromHypersys = jwt.claim<JsonNumber>("hypersys.expires_in").get().longValue()
        val sinceIssued = (System.currentTimeMillis() - jwt.issuedAtTime) / 1000
        return max(fromHypersys - sinceIssued, 0).toInt()
    }

    private fun extract(jwt: JsonWebToken, claim: String) = jwt.claim<String>("hypersys.$claim").get()
}
