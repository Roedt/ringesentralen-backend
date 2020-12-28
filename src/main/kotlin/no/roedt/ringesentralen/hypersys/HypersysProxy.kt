package no.roedt.ringesentralen.hypersys

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.impl.client.HttpClientBuilder
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.util.*
import javax.enterprise.context.Dependent

@Dependent
class HypersysProxy {

    @ConfigProperty(name = "baseURL")
    lateinit var baseURL: String

    val kMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun createHttpPostWithHeader(id: String, secret: String): HttpPost {
        val base64Credentials: String = Base64.getEncoder().encodeToString(("${id}:${secret}").toByteArray())
        val httpPost = HttpPost("$baseURL/api/o/token/")
        httpPost.addHeader("Authorization", "Basic $base64Credentials")
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded")
        return httpPost
    }

    fun httpCall(request: HttpUriRequest) = HttpClientBuilder.create().build()?.execute(request)

    inline fun <reified T> readResponse(response: CloseableHttpResponse?) =
            response?.entity?.content
                    ?.bufferedReader()
                    .use { it?.readText() }
                    ?.let<String, T> { kMapper.readValue(it)}!!


    fun gjennomfoerGetkall(url: String, token: GyldigToken): CloseableHttpResponse? {
        val httpGet = HttpGet("$baseURL/$url")
        httpGet.addHeader("Authorization", "Bearer ${token.access_token}")
        val response = httpCall(httpGet)
        assert(response?.statusLine?.statusCode == 200)
        return response
    }
}