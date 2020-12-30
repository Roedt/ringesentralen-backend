package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.hypersys.externalModel.Organisasjonsledd
import no.roedt.ringesentralen.hypersys.externalModel.Organs
import no.roedt.ringesentralen.hypersys.externalModel.SingleOrgan
import java.net.http.HttpResponse
import javax.enterprise.context.ApplicationScoped

interface HypersysService {
    fun getTokenFromHypersys(): Token
    fun getAlleLokallag(): List<Organisasjonsledd>
    fun getAlleOrganPaaLaagasteNivaa(): List<SingleOrgan>
    fun login(loginRequest: LoginRequest): Token
}

@ApplicationScoped
class HypersysServiceBean(
        val hypersysProxy: HypersysProxy,
        val hypersysTokenVerifier: HypersysTokenVerifier,
        val hypersysLoginBean: HypersysLoginBean
) : HypersysService {

    override fun getTokenFromHypersys() = hypersysTokenVerifier.getTokenFromHypersys()

    override fun getAlleLokallag(): List<Organisasjonsledd> =
        hypersysProxy.readResponse(gjennomfoerGetkall("/org/api/"), ListOrganisasjonsleddTypeReference())

    override fun getAlleOrganPaaLaagasteNivaa(): List<SingleOrgan> = getAlleLokallag().map { toSingleOrgans(it) }.flatten()

    override fun login(loginRequest: LoginRequest): Token = hypersysLoginBean.login(loginRequest)

    private fun toSingleOrgans(lokallag: Organisasjonsledd): List<SingleOrgan> {
        // TODO: Denne må forbetrast. Tar berre med under-under, men vil at denne skal ta med alle som ikkje har organ under seg
        val organs: Organs = hypersysProxy.readResponse(gjennomfoerGetkall("org/api/${lokallag.id}/organ/"), Organs::class.java)
        return organs.organs.map {
            hypersysProxy.readResponse(gjennomfoerGetkall("org/api/${lokallag.id}/organ/${it.id}/"), SingleOrgan::class.java)
        }
    }

    private fun gjennomfoerGetkall(url: String): HttpResponse<String> = hypersysProxy.gjennomfoerGetkall(url, hypersysTokenVerifier.assertGyldigToken())
}