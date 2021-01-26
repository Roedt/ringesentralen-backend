package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.token.GCPSecretManager
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class HypersysSystemTokenVerifier(
        val hypersysProxy: HypersysProxy,
        val gcpSecretManager: GCPSecretManager
) {
    lateinit var token: Token

    @PostConstruct
    fun fetchToken() {
        token = getTokenFromHypersys()
    }

    fun getTokenFromHypersys(): Token {
        val response = hypersysProxy.post(gcpSecretManager.getHypersysClientId(), gcpSecretManager.getHypersysClientSecret(), "grant_type=client_credentials")
        if (response.statusCode() != 200) {
            return hypersysProxy.readResponse(response, UgyldigToken::class.java)
        }
        return hypersysProxy.readResponse(response, GyldigSystemToken::class.java)
    }

    fun assertGyldigSystemToken(): GyldigSystemToken {
        if (token is UgyldigToken) {
            throw RuntimeException("Ugyldig token $token")
        }
        ensureTokenIsValid()
        return token as GyldigSystemToken
    }

    private fun ensureTokenIsValid() {
        if (token is UgyldigToken) {
            return
        }
        if ((token as GyldigSystemToken).expires_in <= 0) {
            fetchToken()
        }
    }
}