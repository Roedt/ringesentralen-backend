package no.roedt.ringesentralen

import jakarta.enterprise.context.Dependent
import no.roedt.hypersys.Token
import no.roedt.hypersys.externalModel.Profile
import no.roedt.hypersys.konvertering.ModelConverter
import no.roedt.hypersys.login.AESUtil
import no.roedt.hypersys.login.HypersysLoginBean
import no.roedt.hypersys.login.LoginRequest
import no.roedt.hypersys.login.LoginService
import no.roedt.hypersys.restClient.HypersysRestClient
import no.roedt.person.Person
import no.roedt.person.PersonService
import no.roedt.ringesentralen.brukere.RingesentralenGroupID
import no.roedt.ringesentralen.ringer.Ringer
import no.roedt.ringesentralen.ringer.RingerService
import no.roedt.token.SecretFactory
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.time.Instant

@Dependent
class RingesentralenLoginBean(
    @RestClient
    hypersysRestClient: HypersysRestClient,
    modelConverter: ModelConverter,
    secretFactory: SecretFactory,
    loginService: LoginService,
    personService: PersonService,
    private val ringerService: RingerService,
    aesUtil: AESUtil
) : HypersysLoginBean(
    hypersysRestClient,
    modelConverter,
    secretFactory,
    loginService,
    personService,
    aesUtil
) {
    override fun login(loginRequest: LoginRequest): Pair<Token, Person?> {
        val token = loginInternal(loginRequest)
        val oppdatertPerson = oppdaterPersoninformasjon(token)
        lagreSomRinger(oppdatertPerson.first)
        return Pair(token, oppdatertPerson.second)
    }

    private fun lagreSomRinger(id: Long) {
        if (ringerService.finnFraPerson(id.toInt()).count() == 0L) {
            ringerService.persist(Ringer(oppretta = Instant.now(), personId = id.toInt()))
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
