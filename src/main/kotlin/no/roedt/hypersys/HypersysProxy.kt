package no.roedt.hypersys

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
import java.util.Base64
import javax.enterprise.context.Dependent

@Dependent
class HypersysProxy(
    @ConfigProperty(name = "hypersysBaseUrl", defaultValue = "")
    private val baseURL: String
) {

    val kMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun post(id: String, secret: String, entity: String, loggingtekst: String): HttpResponse<String> = httpCall(
        HttpRequest.newBuilder()
            .POST(BodyPublishers.ofString(entity))
            .uri(URI.create("$baseURL/api/o/token/").also { log(it, loggingtekst) })
            .headers(
                "Authorization",
                "Basic ${getBase64Credentials(id, secret)}",
                "Content-Type",
                "application/x-www-form-urlencoded"
            )
            .build()
    )

    private fun getBase64Credentials(id: String, secret: String): String =
        Base64.getEncoder().encodeToString(("$id:$secret").toByteArray())

    private fun httpCall(request: HttpRequest): HttpResponse<String> =
        HttpClient.newBuilder().build().send(request, BodyHandlers.ofString())

    inline fun <reified T> readResponse(response: HttpResponse<String>?, responseType: Class<T>): T =
        kMapper.readValue(response?.body(), responseType)

    inline fun <reified T> post(url: String, token: GyldigToken, type: Class<T>) =
        readResponse(gjennomfoerPostkall(url, token), type)

    inline fun <reified T> get(url: String, token: GyldigToken, type: Class<T>) =
        readResponse(gjennomfoerGetkall(url, token), type)

    inline fun <reified T> get(url: String, token: GyldigToken, typeReference: TypeReference<T>): T =
        kMapper.readValue(gjennomfoerGetkall(url, token).body(), typeReference)

    fun gjennomfoerGetkall(url: String, token: GyldigToken): HttpResponse<String> =
        httpCall(get("$baseURL/$url", token)).also { assert(it.statusCode() == 200) }

    fun gjennomfoerPostkall(url: String, token: GyldigToken): HttpResponse<String> =
        httpCall(post("$baseURL/$url", token))

    private fun get(uri: String, token: GyldigToken): HttpRequest =
        HttpRequest.newBuilder()
            .GET()
            .header("Authorization", "Bearer ${token.access_token()}")
            .uri(URI.create(uri).also { log(it, "GET") })
            .build()

    private fun post(uri: String, token: GyldigToken): HttpRequest =
        HttpRequest.newBuilder()
            .POST(BodyPublishers.noBody())
            .header("Authorization", "Bearer ${token.access_token()}")
            .uri(URI.create(uri))
            .build()

    private fun log(uri: URI, tekst: String) {
        println("URI: $uri, tekst: $tekst")
    }
}
