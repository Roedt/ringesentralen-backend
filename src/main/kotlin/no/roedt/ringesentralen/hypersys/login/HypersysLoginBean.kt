package no.roedt.ringesentralen.hypersys.login

import no.roedt.ringesentralen.hypersys.GyldigPersonToken
import no.roedt.ringesentralen.hypersys.HypersysProxy
import no.roedt.ringesentralen.hypersys.ModelConverter
import no.roedt.ringesentralen.hypersys.Token
import no.roedt.ringesentralen.hypersys.UgyldigToken
import no.roedt.ringesentralen.hypersys.externalModel.Profile
import no.roedt.ringesentralen.person.EpostValidator
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.Ringer
import no.roedt.ringesentralen.person.RingerIV1Repository
import no.roedt.ringesentralen.person.RingerRepository
import no.roedt.ringesentralen.token.SecretFactory
import java.net.http.HttpResponse
import java.util.Optional
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
        return hypersysProxy.post(brukerId, brukerSecret, "grant_type=password&username=$brukarnamn&password=$passord", loggingtekst = "brukarinnlogging")
    }

    private fun oppdaterRingerFraaHypersys(token: GyldigPersonToken): Person {
        val profile: Profile = hypersysProxy.get("actor/api/profile/", token, Profile::class.java)
        val convertedPerson  = modelConverter.convert(profile.user, getRolle(profile))

        lagreSomRinger(convertedPerson)
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

    private fun lagreSomRinger(convertedPerson: Person) {
        val id = lagrePerson(convertedPerson)

        if (ringerRepository.find("personId", id.toInt()).count() == 0L) {
            ringerRepository.persist(Ringer(personId = id.toInt()))
        }
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
            ?.let { convertedPerson.groupID = maksBrukergruppe(it, convertedPerson.groupID) }
    }

    private fun maksBrukergruppe(it: Int, groupID: Int): Int =
        when {
            (GroupID.AvslaattRinger.references(it) || GroupID.AvslaattRinger.references(groupID)) -> GroupID.AvslaattRinger.nr
            (GroupID.Admin.references(it) || GroupID.Admin.references(groupID)) -> GroupID.Admin.nr
            (GroupID.LokalGodkjenner.references(it) || GroupID.LokalGodkjenner.references(groupID)) -> GroupID.LokalGodkjenner.nr
            (GroupID.GodkjentRingerMedlemmer.references(it) || GroupID.GodkjentRingerMedlemmer.references(groupID)) -> GroupID.GodkjentRingerMedlemmer.nr
            (GroupID.GodkjentRinger.references(it) || GroupID.GodkjentRinger.references(groupID)) -> GroupID.GodkjentRinger.nr
            (GroupID.UgodkjentRinger.references(it) || GroupID.UgodkjentRinger.references(groupID)) -> GroupID.UgodkjentRinger.nr
            else -> max(it, groupID)
        }
}