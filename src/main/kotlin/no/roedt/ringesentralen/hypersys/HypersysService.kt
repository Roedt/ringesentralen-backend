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
}

@ApplicationScoped
class HypersysServiceBean : HypersysService {
    @ConfigProperty(name = "baseURL")
    lateinit var baseURL: String

    @ConfigProperty(name = "clientId")
    lateinit var clientId: String

    @ConfigProperty(name = "clientSecret")
    lateinit var clientSecret: String

    lateinit var token: Token

    val kMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @PostConstruct
    fun fetchToken() {
        token = getTokenFromHypersys()
    }

    override fun getTokenFromHypersys(): Token  {
        val base64Credentials: String = Base64.getEncoder().encodeToString(("$clientId:$clientSecret").toByteArray())
        val httpPost = HttpPost("$baseURL/api/o/token/")
        httpPost.addHeader("Authorization", "Basic $base64Credentials")
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded")
        httpPost.entity = StringEntity("grant_type=client_credentials")
        val response = httpCall(httpPost)
        assert(response?.statusLine?.statusCode == 200)
        return readResponse(response)
    }

    override fun getAlleLokallag(): List<Organisasjonsledd> = readResponse(gjennomfoerGetkall("/org/api/"))

    override fun getAlleOrganPaaLaagasteNivaa(): List<SingleOrgan> = getAlleLokallag().map { toSingleOrgans(it) }.flatten()

    private fun toSingleOrgans(lokallag: Organisasjonsledd): List<SingleOrgan> {
        val organs: Organs = readResponse(gjennomfoerGetkall("org/api/${lokallag.id}/organ"))
        return organs.organs.map {
            readResponse(gjennomfoerGetkall("org/api/${lokallag.id}/organ/${it.id}"))
        }
    }

    private fun gjennomfoerGetkall(url: String): CloseableHttpResponse? {
        ensureTokenIsValid()
        val httpGet = HttpGet("$baseURL/$url")
        httpGet.addHeader("Authorization", "Bearer ${token.access_token}")
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
        if (token.expires_in <= 0) {
            fetchToken()
        }
    }
    fun httpCall(request: HttpUriRequest) = HttpClientBuilder.create().build()?.execute(request)


}