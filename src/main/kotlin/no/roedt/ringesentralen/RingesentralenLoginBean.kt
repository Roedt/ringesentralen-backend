package no.roedt.ringesentralen

import jakarta.enterprise.context.Dependent
import no.roedt.hypersys.GyldigPersonToken
import no.roedt.hypersys.HypersysProxy
import no.roedt.hypersys.Token
import no.roedt.hypersys.UgyldigToken
import no.roedt.hypersys.externalModel.Profile
import no.roedt.hypersys.konvertering.ModelConverter
import no.roedt.hypersys.login.AESUtil
import no.roedt.hypersys.login.HypersysLoginBean
import no.roedt.hypersys.login.LoginAttemptRepository
import no.roedt.hypersys.login.LoginRequest
import no.roedt.person.Person
import no.roedt.person.PersonService
import no.roedt.ringesentralen.brukere.RingesentralenGroupID
import no.roedt.ringesentralen.ringer.Ringer
import no.roedt.ringesentralen.ringer.RingerRepository
import no.roedt.token.SecretFactory
import java.time.Instant

@Dependent
class RingesentralenLoginBean(
    hypersysProxy: HypersysProxy,
    modelConverter: ModelConverter,
    secretFactory: SecretFactory,
    loginAttemptRepository: LoginAttemptRepository,
    personService: PersonService,
    private val ringerRepository: RingerRepository,
    aesUtil: AESUtil
) : HypersysLoginBean(
    hypersysProxy,
    modelConverter,
    secretFactory,
    loginAttemptRepository,
    personService,
    aesUtil
) {
    override fun login(loginRequest: LoginRequest): Pair<Token, Person?> {
        val token = loginInternal(loginRequest)
        if (token is UgyldigToken) return Pair(token, null)

        val oppdatertPerson = oppdaterPersoninformasjon(token as GyldigPersonToken)
        lagreSomRinger(oppdatertPerson.first)
        return Pair(token, oppdatertPerson.second)
    }

    private fun lagreSomRinger(id: Long) {
        if (ringerRepository.find("personId", id.toInt()).count() == 0L) {
            ringerRepository.persist(Ringer(oppretta = Instant.now(), personId = id.toInt()))
        }
    }

    override fun getRolle(profile: Profile): Int {
        val groupID = super.getRolle(profile)
        if (groupID == -1 || RingesentralenGroupID.isIkkeRegistrertRinger(groupID)) {
            return RingesentralenGroupID.UgodkjentRinger.nr
        }
        return groupID
    }
}
