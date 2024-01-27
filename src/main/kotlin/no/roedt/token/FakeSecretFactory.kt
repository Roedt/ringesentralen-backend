package no.roedt.token

class FakeSecretFactory : SecretFactory {
    override fun getPrivateKey() = "A"

    override fun getFrontendTokenKey() = "AOC2024"

    override fun getHypersysBrukerId() = "C"

    override fun getHypersysBrukerSecret() = "D"

    override fun getHypersysClientId() = "E"

    override fun getHypersysClientSecret() = "F"

    override fun getFrontendSystembruker() = "G"

    override fun getFrontendSystembrukerPassord() = "H"

    override fun getEncryptionKey() = "FfZdV0Cu13lscPPVjYjJGRZmp0afpY3h"

    override fun getTwilioAccountSid() = "I"

    override fun getTwilioAuthToken() = "J"
}
