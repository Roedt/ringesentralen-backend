package no.roedt.ringesentralen.hypersys

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.util.*
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped

interface HypersysService {
    fun getTokenFromHypersys(): Token
    fun getAlleLokallag(): List<Organisasjonsledd>
    fun getAlleOrganPaaLaagasteNivaa(): List<SingleOrgan>
    fun login(loginRequest: LoginRequest): Token
}

@ApplicationScoped
class HypersysServiceBean : HypersysService {
    @ConfigProperty(name = "baseURL")
    lateinit var baseURL: String

    @ConfigProperty(name = "clientId")
    lateinit var clientId: String

    @ConfigProperty(name = "clientSecret")
    lateinit var clientSecret: String

    @ConfigProperty(name = "brukarId")
    lateinit var brukarId: String

    @ConfigProperty(name = "brukarSecret")
    lateinit var brukarSecret: String

    lateinit var token: Token

    val kMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @PostConstruct
    fun fetchToken() {
        token = getTokenFromHypersys()
    }

    override fun getTokenFromHypersys(): Token {
        val httpPost = createHttpPostWithHeader(clientId, clientSecret)
        httpPost.entity = StringEntity("grant_type=client_credentials")
        val response = httpCall(httpPost)
        if (response?.statusLine?.statusCode != 200) {
            return readResponse(response) as UgyldigToken
        }
        return readResponse(response) as GyldigToken
    }

    override fun getAlleLokallag(): List<Organisasjonsledd> = readResponse(gjennomfoerGetkall("/org/api/"))

    override fun getAlleOrganPaaLaagasteNivaa(): List<SingleOrgan> = getAlleLokallag().map { toSingleOrgans(it) }.flatten()

    override fun login(loginRequest: LoginRequest): Token {
        val httpPost = createHttpPostWithHeader(brukarId, brukarSecret)
        httpPost.entity = StringEntity("grant_type=password&username=${loginRequest.brukarnamn}&password=${loginRequest.passord}")
        val response = httpCall(httpPost)
        if (response?.statusLine?.statusCode != 200) {
            return readResponse(response) as UgyldigToken
        }
        return readResponse(response) as GyldigToken
    }

    private fun createHttpPostWithHeader(id: String, secret: String): HttpPost {
        val base64Credentials: String = Base64.getEncoder().encodeToString(("${id}:${secret}").toByteArray())
        val httpPost = HttpPost("$baseURL/api/o/token/")
        httpPost.addHeader("Authorization", "Basic $base64Credentials")
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded")
        return httpPost
    }

    private fun toSingleOrgans(lokallag: Organisasjonsledd): List<SingleOrgan> {
        val organs: Organs = readResponse(gjennomfoerGetkall("org/api/${lokallag.id}/organ"))
        return organs.organs.map {
            readResponse<SingleOrgan>(gjennomfoerGetkall("org/api/${lokallag.id}/organ/${it.id}"))
        }
    }

    private fun gjennomfoerGetkall(url: String): CloseableHttpResponse? {
        ensureTokenIsValid()
        val httpGet = HttpGet("$baseURL/$url")
        httpGet.addHeader("Authorization", "Bearer ${(token as GyldigToken).access_token}")
        val response = httpCall(httpGet)
        assert(response?.statusLine?.statusCode == 200)
        return response
    }

    private inline fun <reified T> readResponse(response: CloseableHttpResponse?) =
            response?.entity?.content
                    ?.bufferedReader()
                    .use { it?.readText() }
                    ?.let<String, T> { kMapper.readValue(it)}!!

    private fun ensureTokenIsValid() {
        if (token is UgyldigToken) {
            return
        }
        if ((token as GyldigToken).expires_in <= 0) {
            fetchToken()
        }
    }
    fun httpCall(request: HttpUriRequest) = HttpClientBuilder.create().build()?.execute(request)


}