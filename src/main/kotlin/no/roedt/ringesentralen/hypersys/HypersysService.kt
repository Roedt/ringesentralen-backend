package no.roedt.ringesentralen.hypersys

import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.entity.StringEntity
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

interface HypersysService {
    fun getTokenFromHypersys(): Token
    fun getAlleLokallag(): List<Organisasjonsledd>
    fun getAlleOrganPaaLaagasteNivaa(): List<SingleOrgan>
    fun login(loginRequest: LoginRequest): Token
}

@ApplicationScoped
class HypersysServiceBean : HypersysService {

    @ConfigProperty(name = "brukarId")
    lateinit var brukarId: String

    @ConfigProperty(name = "brukarSecret")
    lateinit var brukarSecret: String

    lateinit var token: Token

    @Inject
    lateinit var hypersysProxy: HypersysProxy

    @Inject
    lateinit var hypersysTokenVerifier: HypersysTokenVerifier

    override fun getTokenFromHypersys() = hypersysTokenVerifier.getTokenFromHypersys()

    override fun getAlleLokallag(): List<Organisasjonsledd> = hypersysProxy.readResponse(gjennomfoerGetkall("/org/api/"))

    override fun getAlleOrganPaaLaagasteNivaa(): List<SingleOrgan> = getAlleLokallag().map { toSingleOrgans(it) }.flatten()

    override fun login(loginRequest: LoginRequest): Token {
        val httpPost = hypersysProxy.createHttpPostWithHeader(brukarId, brukarSecret)
        httpPost.entity = StringEntity("grant_type=password&username=${loginRequest.brukarnamn}&password=${loginRequest.passord}")
        val response = hypersysProxy.httpCall(httpPost)
        if (response?.statusLine?.statusCode != 200) {
            return hypersysProxy.readResponse(response) as UgyldigToken
        }
        return hypersysProxy.readResponse(response) as GyldigToken
    }

    private fun toSingleOrgans(lokallag: Organisasjonsledd): List<SingleOrgan> {
        val organs: Organs = hypersysProxy.readResponse(gjennomfoerGetkall("org/api/${lokallag.id}/organ"))
        return organs.organs.map {
            hypersysProxy.readResponse<SingleOrgan>(gjennomfoerGetkall("org/api/${lokallag.id}/organ/${it.id}"))
        }
    }

    private fun gjennomfoerGetkall(url: String): CloseableHttpResponse? = hypersysProxy.gjennomfoerGetkall(url, hypersysTokenVerifier.assertGyldigToken())
}