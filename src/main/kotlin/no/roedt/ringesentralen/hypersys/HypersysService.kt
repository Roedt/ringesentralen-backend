package no.roedt.ringesentralen.hypersys

import org.apache.http.client.methods.CloseableHttpResponse
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

    lateinit var token: Token

    @Inject
    lateinit var hypersysProxy: HypersysProxy

    @Inject
    lateinit var hypersysTokenVerifier: HypersysTokenVerifier

    @Inject
    lateinit var hypersysLoginBean: HypersysLoginBean

    override fun getTokenFromHypersys() = hypersysTokenVerifier.getTokenFromHypersys()

    override fun getAlleLokallag(): List<Organisasjonsledd> = hypersysProxy.readResponse(gjennomfoerGetkall("/org/api/"))

    override fun getAlleOrganPaaLaagasteNivaa(): List<SingleOrgan> = getAlleLokallag().map { toSingleOrgans(it) }.flatten()

    override fun login(loginRequest: LoginRequest): Token = hypersysLoginBean.login(loginRequest)

    private fun toSingleOrgans(lokallag: Organisasjonsledd): List<SingleOrgan> {
        val organs: Organs = hypersysProxy.readResponse(gjennomfoerGetkall("org/api/${lokallag.id}/organ"))
        return organs.organs.map {
            hypersysProxy.readResponse<SingleOrgan>(gjennomfoerGetkall("org/api/${lokallag.id}/organ/${it.id}"))
        }
    }

    private fun gjennomfoerGetkall(url: String): CloseableHttpResponse? = hypersysProxy.gjennomfoerGetkall(url, hypersysTokenVerifier.assertGyldigToken())
}