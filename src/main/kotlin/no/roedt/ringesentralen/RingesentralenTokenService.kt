package no.roedt.ringesentralen

import io.smallrye.jwt.build.Jwt
import no.roedt.brukere.GroupID
import no.roedt.brukere.mfa.MFAService
import no.roedt.hypersys.GyldigPersonToken
import no.roedt.hypersys.login.AESUtil
import no.roedt.hypersys.login.HypersysLoginBean
import no.roedt.person.Person
import no.roedt.person.PersonRepository
import no.roedt.ringesentralen.brukere.RingesentralenGroupID
import no.roedt.token.PrivateKeyFactory
import no.roedt.token.SecretFactory
import no.roedt.token.TokenService
import javax.inject.Singleton
import javax.ws.rs.NotAuthorizedException

@Singleton
class RingesentralenTokenService(
    private val personRepository: PersonRepository,
    privateKeyFactory: PrivateKeyFactory,
    secretFactory: SecretFactory,
    hypersysLoginBean: HypersysLoginBean,
    aesUtil: AESUtil,
    mfaService: MFAService
) : TokenService(personRepository, privateKeyFactory, secretFactory, hypersysLoginBean, aesUtil, mfaService) {
    override fun generateBaseToken() = Jwt
        .audience("ringer")
        .issuer("https://ringesentralen.no")
        .subject("Ringesentralen")
        .upn("Ringesentralen")
        .issuedAt(System.currentTimeMillis())
        .expiresAt(System.currentTimeMillis() + tokenExpiryPeriod.toSeconds())

    override fun getRolle(hypersysToken: GyldigPersonToken, person: Person): GroupID {
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
}
