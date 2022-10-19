package no.roedt.token

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FakeSecretFactory : SecretFactory {
    override fun getPrivateKey() = "A"

    override fun getFrontendTokenKey() = "B"

    override fun getHypersysBrukerId() = "C"

    override fun getHypersysBrukerSecret() = "D"

    override fun getHypersysClientId() = "E"

    override fun getHypersysClientSecret() = "F"

    override fun getFrontendSystembruker() = "G"

    override fun getFrontendSystembrukerPassord() = "H"

    override fun getEncryptionKey() = "Hi6ly3ozN8GeOgQzBXGPOQ==:rZChW/juIwZkwo7BitDKTQ=="
}
