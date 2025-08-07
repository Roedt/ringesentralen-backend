package no.roedt.token

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Produces
import no.roedt.brukere.mfa.MFAService
import no.roedt.hypersys.login.AESUtil
import no.roedt.hypersys.login.HypersysLoginBean
import no.roedt.person.PersonService
import no.roedt.token.fake.FakeTokenService
import no.roedt.token.real.RealTokenService
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.time.Duration

@ApplicationScoped
class TokenServiceProducer(
    @ConfigProperty(name = "brukHypersys", defaultValue = "true")
    private val brukHypersys: Boolean,
    private val personService: PersonService,
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
    fun tokenService(): TokenService =
        if (brukHypersys) {
            RealTokenService(
                personService = personService,
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
                tokenExpiryPeriod = tokenExpiryPeriod
            )
        }
}
