package no.roedt.token

import io.smallrye.jwt.build.Jwt
import no.roedt.brukere.GenerellRolle
import no.roedt.brukere.GroupID
import no.roedt.brukere.mfa.MFARequest
import no.roedt.brukere.mfa.MFAService
import no.roedt.hypersys.GyldigPersonToken
import no.roedt.hypersys.Token
import no.roedt.hypersys.UgyldigToken
import no.roedt.hypersys.login.AESUtil
import no.roedt.hypersys.login.HypersysLoginBean
import no.roedt.hypersys.login.LoginRequest
import no.roedt.person.Person
import no.roedt.person.PersonRepository
import no.roedt.ringesentralen.brukere.RingesentralenGroupID
import java.time.Duration
import javax.ws.rs.ForbiddenException
import javax.ws.rs.NotAuthorizedException
import javax.ws.rs.ServiceUnavailableException

class RealTokenService(
    private val personRepository: PersonRepository,
    private val privateKeyFactory: PrivateKeyFactory,
    private val secretFactory: SecretFactory,
    private val hypersysLoginBean: HypersysLoginBean,
    private val aesUtil: AESUtil,
    private val mfaService: MFAService,
    private val tokenExpiryPeriod: Duration
) : TokenService {

    override fun login(loginRequest: LoginRequest): String {
        if (loginRequest.key != secretFactory.getFrontendTokenKey()) {
            throw IllegalArgumentException("Illegal key")
        }

        if (loginRequest.systembruker) {
            return loginSomSystembruker(loginRequest)
        }

        mfaService.verifiserEngangskode(loginRequest)
        val hypersysToken: Pair<Token, Person?> = loginMotHypersys(loginRequest)
        return generateToken(hypersysToken.first as GyldigPersonToken, hypersysToken.second!!)
    }

    private fun loginSomSystembruker(loginRequest: LoginRequest): String {
        if (aesUtil.decrypt(loginRequest.brukarnamn) != secretFactory.getFrontendSystembruker() || aesUtil.decrypt(loginRequest.passord) != secretFactory.getFrontendSystembrukerPassord()) {
            System.err.println(loginRequest)
            throw IllegalArgumentException("Ugyldig brukernavn eller passord")
        }
        return generateBaseToken()
            .groups(GenerellRolle.systembrukerFrontend)
            .claim("hypersys.user_id", personRepository.find("fornavn='Systembruker' and etternavn='Frontend'").firstResult<Person>().hypersysID)
            .sign(privateKeyFactory.readPrivateKey())
    }

    private fun loginMotHypersys(loginRequest: LoginRequest): Pair<Token, Person?> {
        val hypersysToken: Pair<Token, Person?> = try {
            hypersysLoginBean.login(loginRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            throw ServiceUnavailableException("Kunne ikke kontakte Hypersys")
        }
        if (hypersysToken.first is UgyldigToken) throw ForbiddenException((hypersysToken.first as UgyldigToken).error)
        return hypersysToken
    }

    private fun generateToken(hypersysToken: GyldigPersonToken, person: Person): String = generateBaseToken()
        .groups(getGroups(hypersysToken, person))
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
        .expiresAt(System.currentTimeMillis() + tokenExpiryPeriod.toSeconds())

    private fun getGroups(hypersysToken: GyldigPersonToken, person: Person): Set<String> =
        getRolle(hypersysToken, person)
            .roller
            .also { i -> if (i.isEmpty()) println("Fann ingen roller for ${hypersysToken.user_id}") }

    fun getRolle(hypersysToken: GyldigPersonToken, person: Person): GroupID {
        var groupID = RingesentralenGroupID.from(getPersonFromHypersysID(hypersysToken).groupID())
        if (RingesentralenGroupID.isIkkeRegistrertRinger(groupID.nr)) {
            groupID = RingesentralenGroupID.from(person.groupID())
        }
        if (RingesentralenGroupID.erUgyldigRolleForAaBrukeSystemet(groupID)) {
            throw NotAuthorizedException("${hypersysToken.user_id} har ikkje gyldig rolle for å bruke systemet.", "")
        }
        return groupID
    }
    private fun getPersonFromHypersysID(hypersysToken: GyldigPersonToken) =
        personRepository.find("hypersysID", hypersysToken.user_id.toInt()).firstResult<Person>()
    override fun trengerMFA(mfaRequest: MFARequest) = mfaService.trengerMFA(mfaRequest)
    override fun sendMFA(mfaRequest: MFARequest) {
        mfaService.sendMFA(mfaRequest)
    }
}