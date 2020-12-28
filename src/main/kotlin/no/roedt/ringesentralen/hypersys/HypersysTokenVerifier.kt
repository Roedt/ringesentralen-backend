package no.roedt.ringesentralen.hypersys

import org.apache.http.entity.StringEntity
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
        val httpPost = hypersysProxy.createHttpPostWithHeader(clientId, clientSecret)
        httpPost.entity = StringEntity("grant_type=client_credentials")
        val response = hypersysProxy.httpCall(httpPost)
        if (response?.statusLine?.statusCode != 200) {
            return hypersysProxy.readResponse(response) as UgyldigToken
        }
        return hypersysProxy.readResponse(response) as GyldigToken
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