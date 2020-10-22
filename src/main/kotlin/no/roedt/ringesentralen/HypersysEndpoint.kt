package no.roedt.ringesentralen

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import no.roedt.ringesentralen.hypersys.Token
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@ApplicationScoped
@Path("/hypersys")
class HypersysEndpoint(
) {
    @ConfigProperty(name = "tokenEndpoint")
    lateinit var tokenEndpoint: String
   
    @ConfigProperty(name = "clientId")
    lateinit var clientId: String   
    
    @ConfigProperty(name = "clientSecret")
    lateinit var clientSecret: String
    
    val kMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())

    @GET
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    fun getToken(): Token? {
        val base64Credentials: String = Base64.getEncoder().encodeToString(("$clientId:$clientSecret").toByteArray())
        val httpPost = HttpPost(tokenEndpoint)
        httpPost.addHeader("Authorization", "Basic $base64Credentials")
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded")
        httpPost.entity = StringEntity("grant_type=client_credentials")
        val httpClient: CloseableHttpClient? = HttpClientBuilder.create().build()
        val response = httpClient?.execute(httpPost)
        assert(response?.statusLine?.statusCode == 200)
        return response?.entity?.content
                ?.bufferedReader()
                .use { it?.readText() }
                ?.let<String, Token> { kMapper.readValue(it) }
    }
}