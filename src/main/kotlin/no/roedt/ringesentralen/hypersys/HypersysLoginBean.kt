package no.roedt.ringesentralen.hypersys

import org.apache.http.entity.StringEntity
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.Dependent
import javax.inject.Inject

@Dependent
class HypersysLoginBean {

    @Inject
    lateinit var hypersysProxy: HypersysProxy

    @ConfigProperty(name = "brukarId")
    lateinit var brukarId: String

    @ConfigProperty(name = "brukarSecret")
    lateinit var brukarSecret: String

    fun login(loginRequest: LoginRequest): Token {
        val httpPost = hypersysProxy.createHttpPostWithHeader(brukarId, brukarSecret)
        httpPost.entity = StringEntity("grant_type=password&username=${loginRequest.brukarnamn}&password=${loginRequest.passord}")
        val response = hypersysProxy.httpCall(httpPost)
        if (response?.statusLine?.statusCode != 200) {
            return hypersysProxy.readResponse(response) as UgyldigToken
        }
        return hypersysProxy.readResponse(response) as GyldigToken
    }
}