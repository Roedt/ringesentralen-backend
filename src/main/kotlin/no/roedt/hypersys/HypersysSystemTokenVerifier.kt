package no.roedt.hypersys

import no.roedt.token.SecretFactory
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class HypersysSystemTokenVerifier(
    val hypersysProxy: no.roedt.hypersys.HypersysProxy,
    val secretFactory: SecretFactory
) {
    lateinit var token: Token

    @PostConstruct
    fun fetchToken() {
        token = getTokenFromHypersys()
    }

    fun getTokenFromHypersys(): Token {
        val response = hypersysProxy.post(secretFactory.getHypersysClientId(), secretFactory.getHypersysClientSecret(), "grant_type=client_credentials", loggingtekst = "systeminnlogging")
        return if (response.statusCode() != 200) {
            hypersysProxy.readResponse(response, UgyldigToken::class.java)
        } else {
            hypersysProxy.readResponse(response, GyldigSystemToken::class.java)
        }
    }

    fun assertGyldigSystemToken(): GyldigSystemToken {
        if (token is UgyldigToken) throw RuntimeException("Ugyldig token $token")
        ensureTokenIsValid()
        return token as GyldigSystemToken
    }

    private fun ensureTokenIsValid() {
        when {
            token is UgyldigToken -> return
            (token as GyldigSystemToken).expires_in <= 0 -> fetchToken()
        }
    }
}
