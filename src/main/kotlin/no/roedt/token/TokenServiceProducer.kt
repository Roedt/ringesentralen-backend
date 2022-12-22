package no.roedt.token

import no.roedt.brukere.mfa.MFAService
import no.roedt.hypersys.login.AESUtil
import no.roedt.hypersys.login.HypersysLoginBean
import no.roedt.person.PersonRepository
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.time.Duration
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.Produces

@ApplicationScoped
class TokenServiceProducer(
    @ConfigProperty(name = "brukHypersys", defaultValue = "true")
    private val brukHypersys: Boolean,
    private val personRepository: PersonRepository,
    private val privateKeyFactory: PrivateKeyFactory,
    private val secretFactory: SecretFactory,
    private val hypersysLoginBean: HypersysLoginBean,
    private val aesUtil: AESUtil,
    private val mfaService: MFAService,
    @ConfigProperty(name = "token.expiryPeriod")
    private val tokenExpiryPeriod: Duration
) {

    @Produces
    @ApplicationScoped
    fun tokenService(): TokenService = if (brukHypersys) {
        RealTokenService(
            personRepository = personRepository,
            privateKeyFactory = privateKeyFactory,
            secretFactory = secretFactory,
            hypersysLoginBean = hypersysLoginBean,
            aesUtil = aesUtil,
            mfaService = mfaService,
            tokenExpiryPeriod = tokenExpiryPeriod
        )
    } else {
        FakeTokenService(
            secretFactory = secretFactory,
            privateKeyFactory = privateKeyFactory,
            aesUtil = aesUtil,
            tokenExpiryPeriod = tokenExpiryPeriod
        )
    }
}
