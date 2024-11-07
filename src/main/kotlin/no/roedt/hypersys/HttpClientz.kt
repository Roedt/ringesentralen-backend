package no.roedt.hypersys

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers

object HttpClientz {

    fun get(
        uri: String,
        token: GyldigToken
    ): HttpResponse<String> =
        HttpRequest.newBuilder()
            .GET()
            .medToken(token)
            .uri(URI.create(uri).also { println("URI: $uri, tekst: GET") })
            .build()
            .let {
                HttpClient.newBuilder().build().send(it, BodyHandlers.ofString())
            }


    fun post(
        uri: String,
        token: GyldigToken?,
        entity: String? = null,
        additionalHeaders: List<Pair<String, String>> = listOf()
    ): HttpResponse<String>? = HttpRequest.newBuilder()
        .POST(entity?.let { BodyPublishers.ofString(it) } ?: BodyPublishers.noBody())
        .medToken(token)
        .medHeaders(additionalHeaders)
        .uri(URI.create(uri))
        .build()
        .let {
            HttpClient.newBuilder().build().send(it, BodyHandlers.ofString())
        }
}

fun HttpRequest.Builder.medToken(token: GyldigToken?) = token?.accessToken()?.let { this.header("Authorization", "Bearer $it") } ?: this

fun HttpRequest.Builder.medHeaders(headers: List<Pair<String, String>>): HttpRequest.Builder {
    var builder = this
    headers.forEach {
        builder = builder.header(it.first, it.second)
    }
    return builder
}