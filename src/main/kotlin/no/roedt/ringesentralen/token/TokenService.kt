package no.roedt.ringesentralen.token

import io.smallrye.jwt.build.Jwt
import no.roedt.ringesentralen.Roles
import no.roedt.ringesentralen.hypersys.*
import no.roedt.ringesentralen.person.EpostValidator
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.time.Duration
import javax.enterprise.context.RequestScoped
import javax.ws.rs.ForbiddenException
import javax.ws.rs.ServiceUnavailableException


@RequestScoped
class TokenService(
    private val personRepository: PersonRepository,
    private val privateKeyFactory: PrivateKeyFactory,
    private val secretFactory: SecretFactory,
    private val hypersysLoginBean: HypersysLoginBean
) {

    @ConfigProperty(name = "token.expiryPeriod")
    lateinit var tokenExpiryPeriod: Duration

    fun login(loginRequest: LoginRequest): String {
        if (loginRequest.key != secretFactory.getFrontendTokenKey()) {
            throw IllegalArgumentException("Illegal key")
        }

        if (loginRequest.systembruker) {
            if (loginRequest.brukarnamn != secretFactory.getFrontendSystembruker() || loginRequest.passord != secretFactory.getFrontendSystembrukerPassord()) {
                System.err.println(loginRequest)
                throw IllegalArgumentException("Ugyldig brukernavn eller passord")
            }
            return generateBaseToken()
                .groups(Roles.systembrukerFrontend)
                .claim("hypersys.user_id", personRepository.find("fornavn='Systembruker' and etternavn='Frontend'").firstResult<Person>().hypersysID)
                .sign(privateKeyFactory.readPrivateKey())
        }

        EpostValidator.validate(loginRequest.brukarnamn)

        val hypersysToken: Token = try {
            hypersysLoginBean.login(loginRequest)
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw ServiceUnavailableException("Kunne ikke kontakte Hyperys")
        }
        if (hypersysToken is UgyldigToken)
            throw ForbiddenException(hypersysToken.error)
        return generateToken(hypersysToken as GyldigPersonToken)
    }

    private fun generateToken(hypersysToken: GyldigPersonToken): String = generateBaseToken()
        .groups(getGroups(hypersysToken))
        .claim("hypersys.token_type", hypersysToken.token_type)
        .claim("hypersys.scope", hypersysToken.scope)
        .claim("hypersys.access_token", hypersysToken.access_token)
        .claim("hypersys.expires_in", hypersysToken.expires_in)
        .claim("hypersys.refresh_token", hypersysToken.refresh_token)
        .claim("hypersys.user_id", hypersysToken.user_id)
        .sign(privateKeyFactory.readPrivateKey())

    private fun generateBaseToken() = Jwt
            .audience("ringer")
            .issuer("https://ringesentralen.no")
            .subject("Ringesentralen")
            .upn("Ringesentralen")
            .issuedAt(System.currentTimeMillis())
            .expiresAt(getTokenExpiresAt())

    private fun getTokenExpiresAt() = System.currentTimeMillis() + tokenExpiryPeriod.toSeconds()

    private fun getGroups(hypersysToken: GyldigPersonToken): Set<String> = GroupID.from(getPersonFromHypersysID(hypersysToken).groupID).roller

    private fun getPersonFromHypersysID(hypersysToken: GyldigPersonToken) =
        personRepository.find("hypersysID", hypersysToken.user_id.toInt()).firstResult<Person>()
}
