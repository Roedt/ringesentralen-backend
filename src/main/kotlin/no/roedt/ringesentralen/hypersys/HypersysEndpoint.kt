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
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import java.util.*
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@ApplicationScoped
@Path("/hypersys")
@Tag(name = "Hypersys-integrasjon")
class HypersysEndpoint {
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

    @GET
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    fun getTokenFromHypersys(): Token {
        val base64Credentials: String = Base64.getEncoder().encodeToString(("$clientId:$clientSecret").toByteArray())
        val httpPost = HttpPost("$baseURL/api/o/token/")
        httpPost.addHeader("Authorization", "Basic $base64Credentials")
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded")
        httpPost.entity = StringEntity("grant_type=client_credentials")
        val response = httpCall(httpPost)
        assert(response?.statusLine?.statusCode == 200)
        return readResponse(response)?.let<String, Token> { kMapper.readValue(it) }!!
    }

    private fun readResponse(response: CloseableHttpResponse?) =
            response?.entity?.content
                    ?.bufferedReader()
                    .use { it?.readText() }

    @GET
    @Path("/lokallag")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAlleLokallag() : List<Organisasjonsledd> =
            readResponse(gjennomfoerGetkall("/org/api/", 200))?.let<String, List<Organisasjonsledd>> { kMapper.readValue(it) }!!

    private fun gjennomfoerGetkall(url: String, forventaStatuskode: Int): CloseableHttpResponse? {
        ensureTokenIsValid()
        val httpGet = HttpGet("$baseURL/$url")
        httpGet.addHeader("Authorization", "Bearer ${token.access_token}")
        val response = httpCall(httpGet)
        assert(response?.statusLine?.statusCode == forventaStatuskode)
        return response
    }

    private fun ensureTokenIsValid() {
        if (token.expires_in <= 0) {
            fetchToken()
        }
    }

    fun httpCall(request: HttpUriRequest) = HttpClientBuilder.create().build()?.execute(request)
}