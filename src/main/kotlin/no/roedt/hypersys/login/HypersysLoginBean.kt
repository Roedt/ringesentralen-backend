package no.roedt.hypersys.login

import no.roedt.hypersys.GyldigPersonToken
import no.roedt.hypersys.HypersysProxy
import no.roedt.hypersys.Token
import no.roedt.hypersys.UgyldigToken
import no.roedt.hypersys.externalModel.Profile
import no.roedt.hypersys.konvertering.ModelConverter
import no.roedt.person.EpostValidator
import no.roedt.person.Oppdateringskilde
import no.roedt.person.Person
import no.roedt.person.PersonService
import no.roedt.token.SecretFactory
import java.net.http.HttpResponse

abstract class HypersysLoginBean(
    private val hypersysProxy: HypersysProxy,
    private val modelConverter: ModelConverter,
    private val secretFactory: SecretFactory,
    private val loginService: LoginService,
    private val personService: PersonService,
    private val aesUtil: AESUtil
) {
    abstract fun login(loginRequest: LoginRequest): Pair<Token, Person?>

    protected fun loginInternal(loginRequest: LoginRequest): Token {
        val response = postLogin(loginRequest)
        if (response.statusCode() != 200) return hypersysProxy.readResponse(response, UgyldigToken::class.java)
        return hypersysProxy.readResponse(response, GyldigPersonToken::class.java)
    }

    private fun postLogin(loginRequest: LoginRequest): HttpResponse<String> {
        val brukerId = secretFactory.getHypersysBrukerId()
        val brukerSecret = secretFactory.getHypersysBrukerSecret()
        val brukarnamn = aesUtil.decrypt(loginRequest.brukarnamn).also { EpostValidator.validate(it) }
        val passord = aesUtil.decrypt(loginRequest.passord)
        return hypersysProxy.post(
            brukerId,
            brukerSecret,
            "grant_type=password&username=$brukarnamn&password=$passord",
            loggingtekst = "brukarinnlogging"
        )
    }

    protected fun oppdaterPersoninformasjon(token: GyldigPersonToken): Pair<Long, Person> {
        val profile: Profile = hypersysProxy.get("actor/api/profile/", token, Profile::class.java)
        val convertedPerson = modelConverter.convert(profile.user, getRolle(profile))
        return Pair(lagrePerson(convertedPerson), convertedPerson)
            .also { loginService.persist(LoginAttempt(hypersysID = profile.user.id)) }
    }

    protected open fun getRolle(profile: Profile): Int {
        return personService.finnFraHypersysId(profile.user.id)
            .singleResultOptional<Person>()
            .map { it.groupID() }
            .orElse(-1)
    }

    private fun lagrePerson(convertedPerson: Person): Long {
        personService.save(convertedPerson, Oppdateringskilde.Hypersys)
        var id = convertedPerson.id
        if (id == null) personService.finnFraEpost(convertedPerson.email).firstResult<Person>().id.also { id = it }
        return id.toLong()
    }
}
