package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.hypersys.externalModel.Profile
import no.roedt.ringesentralen.person.*
import no.roedt.ringesentralen.token.SecretFactory
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
    private val ringerIV1Repository: RingerIV1Repository
) {
    fun login(loginRequest: LoginRequest): Token {
        val brukerId = secretFactory.getHypersysBrukerId()
        val brukerSecret = secretFactory.getHypersysBrukerSecret()
        val response = hypersysProxy.post(brukerId, brukerSecret, "grant_type=password&username=${loginRequest.brukarnamn}&password=${loginRequest.passord}", loggingtekst = "brukarinnlogging")
        if (response.statusCode() != 200) {
            return hypersysProxy.readResponse(response, UgyldigToken::class.java)
        }

        val gyldigToken = hypersysProxy.readResponse(response, GyldigPersonToken::class.java)
        oppdaterRingerFraaHypersys(gyldigToken)
        return gyldigToken
    }

    private fun oppdaterRingerFraaHypersys(token: GyldigPersonToken) {
        val profile: Profile = hypersysProxy.get("actor/api/profile/", token, Profile::class.java)
        val convertedPerson  = modelConverter.convert(profile.user, GroupID.UgodkjentRinger.nr)

        val id = lagrePerson(convertedPerson)

        if (ringerRepository.find("personId", id.toInt()).count() == 0L) {
            ringerRepository.persist(Ringer(personId = id.toInt()))
        }

        oppdaterBrukergruppeFraV1(convertedPerson)

        loginAttemptRepository.persist(LoginAttempt(hypersysID = profile.user.id))
    }

    private fun lagrePerson(convertedPerson: Person): Long {
        personRepository.save(convertedPerson)
        var id = convertedPerson.id
        if (id == null) personRepository.find("email", convertedPerson.email).firstResult<Person>().id.also { id = it }
        return id
    }

    private fun oppdaterBrukergruppeFraV1(convertedPerson: Person) {
        ringerIV1Repository.find("telefonnummer", convertedPerson.telefonnummer?.replace("+47", ""))
            .list<RingerIV1>()
            .map { it.brukergruppe }
            .firstOrNull()
            ?.let { convertedPerson.groupID = max(it, convertedPerson.groupID) }
    }
}