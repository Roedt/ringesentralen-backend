package no.roedt.ringesentralen.token

import io.smallrye.jwt.build.Jwt
import no.roedt.ringesentralen.hypersys.*
import no.roedt.ringesentralen.person.EpostValidator
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.time.Duration
import javax.enterprise.context.RequestScoped
import javax.ws.rs.ForbiddenException


@RequestScoped
class TokenGenerator(
    private val hypersysService: HypersysService,
    private val personRepository: PersonRepository,
    private val privateKeyFactory: PrivateKeyFactory,
    private val gcpSecretManager: GCPSecretManager
) {

    @ConfigProperty(name = "token.expiryPeriod")
    lateinit var tokenExpiryPeriod: Duration

    fun login(loginRequest: LoginRequest): String {
        if (loginRequest.key != gcpSecretManager.getFrontendTokenKey()) {
            throw IllegalArgumentException("Illegal key")
        }
        EpostValidator.validate(loginRequest.brukarnamn)

        val hypersysToken: Token = hypersysService.login(loginRequest)
        if (hypersysToken is UgyldigToken)
            throw ForbiddenException(hypersysToken.error)
        return generateToken(hypersysToken as GyldigPersonToken)
    }

    private fun generateToken(hypersysToken: GyldigPersonToken): String = Jwt
        .audience("ringer")
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

    private fun getGroups(hypersysToken: GyldigPersonToken): Set<String> = GroupID.from(getPersonFromHypersysID(hypersysToken).groupID).roller

    private fun getPersonFromHypersysID(hypersysToken: GyldigPersonToken) =
        personRepository.find("hypersysID", hypersysToken.user_id.toInt()).firstResult<Person>()
}
