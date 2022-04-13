package no.roedt.hypersys.login

import no.roedt.hypersys.GyldigPersonToken
import no.roedt.hypersys.ModelConverter
import no.roedt.hypersys.Token
import no.roedt.hypersys.UgyldigToken
import no.roedt.hypersys.externalModel.Profile
import no.roedt.person.EpostValidator
import no.roedt.person.Person
import no.roedt.person.PersonRepository
import no.roedt.ringesentralen.brukere.RingesentralenGroupID
import no.roedt.ringesentralen.person.Ringer
import no.roedt.ringesentralen.person.RingerIV1Repository
import no.roedt.ringesentralen.person.RingerRepository
import no.roedt.token.SecretFactory
import java.net.http.HttpResponse
import java.time.Instant
import java.util.Optional
import javax.enterprise.context.Dependent

@Dependent
class HypersysLoginBean( // TODO: Gå over denne klassa og skil ut det ringespesifikke frå det generelle
    private val hypersysProxy: no.roedt.hypersys.HypersysProxy,
    private val modelConverter: ModelConverter,
    private val secretFactory: SecretFactory,
    private val loginAttemptRepository: LoginAttemptRepository,
    private val personRepository: PersonRepository,
    private val ringerRepository: RingerRepository,
    private val ringerIV1Repository: RingerIV1Repository,
    private val aesUtil: AESUtil
) {
    fun login(loginRequest: LoginRequest): Pair<Token, Person?> {
        val response = postLogin(loginRequest)
        if (response.statusCode() != 200) {
            return Pair(hypersysProxy.readResponse(response, UgyldigToken::class.java), null)
        }

        val gyldigToken = hypersysProxy.readResponse(response, GyldigPersonToken::class.java)
        val person = oppdaterRingerFraaHypersys(gyldigToken)
        return Pair(gyldigToken, person)
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

    private fun oppdaterRingerFraaHypersys(token: GyldigPersonToken): Person {
        val profile: Profile = hypersysProxy.get("actor/api/profile/", token, Profile::class.java)
        val convertedPerson = modelConverter.convert(profile.user, getRolle(profile))

        lagreSomRinger(convertedPerson)
        oppdaterBrukergruppeFraV1(convertedPerson)
        loginAttemptRepository.persist(LoginAttempt(hypersysID = profile.user.id))
        return convertedPerson
    }

    private fun getRolle(profile: Profile): Int {
        val groupID: Optional<Int> = personRepository.find("hypersysID=?1", profile.user.id)
            .singleResultOptional<Person>()
            .map { it.groupID() }
        if (!groupID.isPresent || RingesentralenGroupID.isIkkeRegistrertRinger(groupID.get())) {
            return RingesentralenGroupID.UgodkjentRinger.nr
        }
        return groupID.get()
    }

    private fun lagreSomRinger(convertedPerson: Person) {
        val id = lagrePerson(convertedPerson)

        if (ringerRepository.find("personId", id.toInt()).count() == 0L) {
            ringerRepository.persist(Ringer(oppretta = Instant.now(), personId = id.toInt()))
        }
    }

    private fun lagrePerson(convertedPerson: Person): Long {
        personRepository.save(convertedPerson)
        var id = convertedPerson.id
        if (id == null) personRepository.find("email", convertedPerson.email).firstResult<Person>().id.also { id = it }
        return id.toLong()
    }

    private fun oppdaterBrukergruppeFraV1(convertedPerson: Person) {
        ringerIV1Repository.list("telefonnummer", convertedPerson.telefonnummer?.replace("+47", ""))
            .map { it.brukergruppe }
            .firstOrNull()
            ?.let { convertedPerson.setGroupID(RingesentralenGroupID.maks(it, convertedPerson.groupID())) }
    }
}
