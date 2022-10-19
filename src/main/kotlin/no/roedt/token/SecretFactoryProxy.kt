package no.roedt.token

import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class SecretFactoryProxy(
    @ConfigProperty(name = "brukHypersys", defaultValue = "true")
    private val brukHypersys: Boolean,
    private val gcpSecretManager: GCPSecretManager,
    private val fakeSecretFactory: FakeSecretFactory
) : SecretFactory {
    override fun getPrivateKey() = if (brukHypersys) gcpSecretManager.getPrivateKey() else fakeSecretFactory.getPrivateKey()

    override fun getFrontendTokenKey() = if (brukHypersys) gcpSecretManager.getFrontendTokenKey() else fakeSecretFactory.getFrontendTokenKey()

    override fun getHypersysBrukerId() = if (brukHypersys) gcpSecretManager.getHypersysBrukerId() else fakeSecretFactory.getHypersysBrukerId()

    override fun getHypersysBrukerSecret() = if (brukHypersys) gcpSecretManager.getHypersysBrukerSecret() else fakeSecretFactory.getHypersysBrukerSecret()

    override fun getHypersysClientId() = if (brukHypersys) gcpSecretManager.getHypersysClientId() else fakeSecretFactory.getHypersysClientId()

    override fun getHypersysClientSecret() = if (brukHypersys) gcpSecretManager.getHypersysClientSecret() else fakeSecretFactory.getHypersysClientSecret()

    override fun getFrontendSystembruker() = if (brukHypersys) gcpSecretManager.getFrontendSystembruker() else fakeSecretFactory.getFrontendSystembruker()

    override fun getFrontendSystembrukerPassord() = if (brukHypersys) gcpSecretManager.getFrontendSystembrukerPassord() else fakeSecretFactory.getFrontendSystembrukerPassord()

    override fun getEncryptionKey() = if (brukHypersys) gcpSecretManager.getEncryptionKey() else fakeSecretFactory.getEncryptionKey()
}
