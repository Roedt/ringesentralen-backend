package no.roedt.hypersys

import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import no.roedt.hypersys.ObjectMapper.kMapper
import no.roedt.token.SecretFactory
import org.eclipse.microprofile.faulttolerance.Fallback
import org.eclipse.microprofile.faulttolerance.Timeout

@ApplicationScoped
class HypersysSystemTokenVerifier(
    val hypersysClient: HypersysClient,
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
        val response =
            hypersysClient.post(
                id = secretFactory.getHypersysClientId(),
                secret = secretFactory.getHypersysClientSecret(),
                entity = "grant_type=client_credentials",
                loggingtekst = "systeminnlogging",
                url = "api/o/token/"
            )
        return if (response.statusCode() != 200) {
            kMapper.readValue(response.body(), UgyldigToken::class.java)
        } else {
            kMapper.readValue(response.body(), GyldigSystemToken::class.java)
        }
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
