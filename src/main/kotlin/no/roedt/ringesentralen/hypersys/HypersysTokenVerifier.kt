package no.roedt.ringesentralen.hypersys

import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class HypersysTokenVerifier(
        val hypersysProxy: HypersysProxy
) {

    @ConfigProperty(name = "clientId")
    lateinit var clientId: String

    @ConfigProperty(name = "clientSecret")
    lateinit var clientSecret: String

    lateinit var token: Token

    @PostConstruct
    fun fetchToken() {
        token = getTokenFromHypersys()
    }

    fun getTokenFromHypersys(): Token {
        val request = hypersysProxy.createHttpPostWithHeader(clientId, clientSecret, "grant_type=client_credentials")
        val response = hypersysProxy.httpCall(request)
        if (response.statusCode() != 200) {
            return hypersysProxy.readResponse(response, UgyldigToken::class.java)
        }
        return hypersysProxy.readResponse(response, GyldigToken::class.java)
    }

    fun assertGyldigToken(): GyldigToken {
        if (token is UgyldigToken) {
            throw RuntimeException("Ugyldig token $token")
        }
        ensureTokenIsValid()
        return token as GyldigToken
    }

    private fun ensureTokenIsValid() {
        if (token is UgyldigToken) {
            return
        }
        if ((token as GyldigToken).expires_in <= 0) {
            fetchToken()
        }
    }
}