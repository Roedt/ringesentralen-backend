package no.roedt.ringesentralen.hypersys

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.util.*
import javax.enterprise.context.Dependent

@Dependent
class HypersysProxy {

    @ConfigProperty(name = "baseURL")
    lateinit var baseURL: String

    val kMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun createHttpPostWithHeader(id: String, secret: String, entity: String): HttpRequest {
        val base64Credentials: String = Base64.getEncoder().encodeToString(("${id}:${secret}").toByteArray())
        return HttpRequest.newBuilder().POST(BodyPublishers.ofString(entity)).uri(URI.create("$baseURL/api/o/token/"))
            .headers("Authorization", "Basic $base64Credentials", "Content-Type", "application/x-www-form-urlencoded")
            .build()
    }

    fun httpCall(request: HttpRequest): HttpResponse<String> = HttpClient.newBuilder().build().send(request, BodyHandlers.ofString())

    inline fun <reified T> readResponse(response: HttpResponse<String>?, responseType: Class<T>): T =
        kMapper.readValue(response?.body(), responseType)

    inline fun <reified T> readResponse(response: HttpResponse<String>?, typeReference: TypeReference<T>): T =
        kMapper.readValue(response?.body(), typeReference)


    fun gjennomfoerGetkall(url: String, token: GyldigToken): HttpResponse<String> {
        val httpGet = httpGet("$baseURL/$url", token)
        val response = httpCall(httpGet)
        assert(response.statusCode() == 200)
        return response
    }

    private fun httpGet(uri: String, token: GyldigToken): HttpRequest = HttpRequest.newBuilder().GET().header("Authorization", "Bearer ${token.access_token}").uri(URI.create(uri)).build()
}