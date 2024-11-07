package no.roedt.hypersys

import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import no.roedt.hypersys.restClient.HypersysRestClient
import no.roedt.token.SecretFactory
import org.eclipse.microprofile.faulttolerance.Fallback
import org.eclipse.microprofile.faulttolerance.Timeout
import java.util.Base64

@ApplicationScoped
class HypersysSystemTokenVerifier(
    val hypersysRestClient: HypersysRestClient,
    val secretFactory: SecretFactory
) {
    private lateinit var token: Token

    @PostConstruct
    fun fetchToken() {
        token = getTokenFromHypersys()
    }

    @Timeout(value = 10000L)
    @Fallback(fallbackMethod = "settTokenUgyldig")
    fun getTokenFromHypersys(): Token {
        val id = secretFactory.getHypersysClientId()
        val secret = secretFactory.getHypersysClientSecret()
        return hypersysRestClient.tokenSystem(
            base64Credentials = Base64.getEncoder().encodeToString(("$id:$secret").toByteArray())
        )
    }

    private fun settTokenUgyldig(): Token = UgyldigToken(error = "Kunne ikke hente token fra Hypersys")

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
