package no.roedt.hypersys

import jakarta.enterprise.context.Dependent
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.net.http.HttpResponse
import java.util.Base64

@Dependent
class HypersysClient(
    @ConfigProperty(name = "hypersysBaseUrl", defaultValue = "")
    private val baseURL: String
) {

    fun post(
        id: String,
        secret: String,
        entity: String,
        loggingtekst: String,
        url: String
    ): HttpResponse<String> =
        HttpClientz.post(
            uri = url,
            token = null,
            entity = entity,
            additionalHeaders = listOf(
                "Authorization" to "Basic ${getBase64Credentials(id, secret)}",
                "Content-Type" to "application/x-www-form-urlencoded")
        )!!.also { println("URI: $url, tekst: $loggingtekst") }

    private fun getBase64Credentials(
        id: String,
        secret: String
    ): String = Base64.getEncoder().encodeToString(("$id:$secret").toByteArray())

    fun gjennomfoerGetkall(url: String, token: GyldigToken) = HttpClientz.get("$baseURL/$url", token)

    fun gjennomfoerPostkall(url: String, token: GyldigToken) = HttpClientz.post("$baseURL/$url", token)

}
