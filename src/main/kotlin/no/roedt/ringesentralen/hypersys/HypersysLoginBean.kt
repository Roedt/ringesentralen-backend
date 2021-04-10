package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.hypersys.externalModel.Profile
import no.roedt.ringesentralen.person.*
import no.roedt.ringesentralen.token.SecretFactory
import java.util.*
import javax.enterprise.context.Dependent
import kotlin.math.max

@Dependent
class HypersysLoginBean(
    private val hypersysProxy: HypersysProxy,
    private val modelConverter: ModelConverter,
    private val secretFactory: SecretFactory,
    private val loginAttemptRepository: LoginAttemptRepository,
    private val personRepository: PersonRepository,
    private val ringerRepository: RingerRepository,
    private val ringerIV1Repository: RingerIV1Repository,
    private val aesUtil: AESUtil
) {
    fun login(loginRequest: LoginRequest): Pair<Token, Person?> {
        val brukerId = secretFactory.getHypersysBrukerId()
        val brukerSecret = secretFactory.getHypersysBrukerSecret()
        val brukarnamn = aesUtil.decrypt(loginRequest.brukarnamn).also { EpostValidator.validate(it) }
        val passord = aesUtil.decrypt(loginRequest.passord)
        val response = hypersysProxy.post(brukerId, brukerSecret, "grant_type=password&username=$brukarnamn&password=$passord", loggingtekst = "brukarinnlogging")
        if (response.statusCode() != 200) {
            return Pair(hypersysProxy.readResponse(response, UgyldigToken::class.java), null)
        }

        val gyldigToken = hypersysProxy.readResponse(response, GyldigPersonToken::class.java)
        val person = oppdaterRingerFraaHypersys(gyldigToken)
        return Pair(gyldigToken, person)
    }

    private fun oppdaterRingerFraaHypersys(token: GyldigPersonToken): Person {
        val profile: Profile = hypersysProxy.get("actor/api/profile/", token, Profile::class.java)
        val convertedPerson  = modelConverter.convert(profile.user, getRolle(profile))

        val id = lagrePerson(convertedPerson)

        if (ringerRepository.find("personId", id.toInt()).count() == 0L) {
            ringerRepository.persist(Ringer(personId = id.toInt()))
        }

        oppdaterBrukergruppeFraV1(convertedPerson)

        loginAttemptRepository.persist(LoginAttempt(hypersysID = profile.user.id))
        return convertedPerson
    }

    private fun getRolle(profile: Profile): Int {
        val groupID: Optional<Int> = personRepository.find("hypersysID=?1", profile.user.id)
            .singleResultOptional<Person>()
            .map { it.groupID }
        if (!groupID.isPresent || GroupID.isIkkeRegistrertRinger(groupID.get())) {
            return GroupID.UgodkjentRinger.nr
        }
        return groupID.get()
    }

    private fun lagrePerson(convertedPerson: Person): Long {
        personRepository.save(convertedPerson)
        var id = convertedPerson.id
        if (id == null) personRepository.find("email", convertedPerson.email).firstResult<Person>().id.also { id = it }
        return id
    }

    private fun oppdaterBrukergruppeFraV1(convertedPerson: Person) {
        ringerIV1Repository.list("telefonnummer", convertedPerson.telefonnummer?.replace("+47", ""))
            .map { it.brukergruppe }
            .firstOrNull()
            ?.let { convertedPerson.groupID = max(it, convertedPerson.groupID) }
    }
}