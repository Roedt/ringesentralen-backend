package no.roedt.ringesentralen.hypersys

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import no.roedt.ringesentralen.token.GCPSecretManager
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.util.*
import javax.annotation.PostConstruct
import javax.enterprise.context.Dependent

@Dependent
class HypersysProxy(private val gcpSecretManager: GCPSecretManager) {

    lateinit var baseURL: String

    @PostConstruct
    fun hentBaseURL() {
        baseURL = gcpSecretManager.getHypersysBaseURL()
    }

    val kMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun post(id: String, secret: String, entity: String): HttpResponse<String> {
        val base64Credentials: String = Base64.getEncoder().encodeToString(("${id}:${secret}").toByteArray())
        val request = HttpRequest.newBuilder().POST(BodyPublishers.ofString(entity)).uri(URI.create("$baseURL/api/o/token/"))
            .headers("Authorization", "Basic $base64Credentials", "Content-Type", "application/x-www-form-urlencoded")
            .build()
        return httpCall(request)
    }

    private fun httpCall(request: HttpRequest): HttpResponse<String> = HttpClient.newBuilder().build().send(request, BodyHandlers.ofString())

    inline fun <reified T> readResponse(response: HttpResponse<String>?, responseType: Class<T>): T =
        kMapper.readValue(response?.body(), responseType)

    inline fun <reified T> get(url: String, token: GyldigToken, type: Class<T>) = readResponse(gjennomfoerGetkall(url, token), type)

    inline fun <reified T> get(url: String, token: GyldigToken, typeReference: TypeReference<T>): T =
        kMapper.readValue(gjennomfoerGetkall(url, token).body(), typeReference)


    fun gjennomfoerGetkall(url: String, token: GyldigToken): HttpResponse<String> {
        val response = httpCall(get("$baseURL/$url", token))
        assert(response.statusCode() == 200)
        return response
    }

    private fun get(uri: String, token: GyldigToken): HttpRequest = HttpRequest.newBuilder().GET().header("Authorization", "Bearer ${token.access_token()}").uri(URI.create(uri)).build()
}