package no.roedt.ringesentralen

import no.roedt.hypersys.GyldigPersonToken
import no.roedt.hypersys.HypersysProxy
import no.roedt.hypersys.ModelConverter
import no.roedt.hypersys.Token
import no.roedt.hypersys.UgyldigToken
import no.roedt.hypersys.externalModel.Profile
import no.roedt.hypersys.login.AESUtil
import no.roedt.hypersys.login.HypersysLoginBean
import no.roedt.hypersys.login.LoginAttemptRepository
import no.roedt.hypersys.login.LoginRequest
import no.roedt.person.Person
import no.roedt.person.PersonRepository
import no.roedt.ringesentralen.brukere.RingesentralenGroupID
import no.roedt.ringesentralen.person.Ringer
import no.roedt.ringesentralen.person.RingerIV1Repository
import no.roedt.ringesentralen.person.RingerRepository
import no.roedt.token.SecretFactoryProxy
import java.time.Instant
import javax.enterprise.context.Dependent

@Dependent
class RingesentralenLoginBean(
    hypersysProxy: HypersysProxy,
    modelConverter: ModelConverter,
    secretFactory: SecretFactoryProxy,
    loginAttemptRepository: LoginAttemptRepository,
    personRepository: PersonRepository,
    private val ringerRepository: RingerRepository,
    private val ringerIV1Repository: RingerIV1Repository,
    aesUtil: AESUtil
) : HypersysLoginBean(
    hypersysProxy,
    modelConverter,
    secretFactory,
    loginAttemptRepository,
    personRepository,
    aesUtil
) {
    override fun login(loginRequest: LoginRequest): Pair<Token, Person?> {
        val token = loginInternal(loginRequest)
        if (token is UgyldigToken) return Pair(token, null)

        val oppdatertPerson = oppdaterPersoninformasjon(token as GyldigPersonToken)
        lagreSomRinger(oppdatertPerson.first)
        oppdaterBrukergruppeFraV1(oppdatertPerson.second)
        return Pair(token, oppdatertPerson.second)
    }

    private fun lagreSomRinger(id: Long) {
        if (ringerRepository.find("personId", id.toInt()).count() == 0L) {
            ringerRepository.persist(Ringer(oppretta = Instant.now(), personId = id.toInt()))
        }
    }

    private fun oppdaterBrukergruppeFraV1(convertedPerson: Person) {
        ringerIV1Repository.list("telefonnummer", convertedPerson.telefonnummer?.replace("+47", ""))
            .map { it.brukergruppe }
            .firstOrNull()
            ?.let { convertedPerson.setGroupID(RingesentralenGroupID.maks(it, convertedPerson.groupID())) }
    }

    override fun getRolle(profile: Profile): Int {
        val groupID = super.getRolle(profile)
        if (groupID == -1 || RingesentralenGroupID.isIkkeRegistrertRinger(groupID)) {
            return RingesentralenGroupID.UgodkjentRinger.nr
        }
        return groupID
    }
}
