package no.roedt.token

interface SecretFactory {
    fun getPrivateKey(): String

    fun getFrontendTokenKey(): String

    fun getHypersysBrukerId(): String

    fun getHypersysBrukerSecret(): String

    fun getHypersysClientId(): String

    fun getHypersysClientSecret(): String

    fun getFrontendSystembruker(): String

    fun getFrontendSystembrukerPassord(): String

    fun getEncryptionKey(): String

    fun getTwilioAccountSid(): String

    fun getTwilioAuthToken(): String
}
