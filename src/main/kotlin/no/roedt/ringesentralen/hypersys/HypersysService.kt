package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.hypersys.externalModel.Organisasjonsledd
import no.roedt.ringesentralen.hypersys.externalModel.Organs
import no.roedt.ringesentralen.hypersys.externalModel.SingleOrgan
import org.eclipse.microprofile.jwt.JsonWebToken
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

interface HypersysService {
    fun getAlleLokallag(): List<Organisasjonsledd>
    fun getAlleOrganPaaLaagasteNivaa(): List<SingleOrgan>
    fun login(loginRequest: LoginRequest): Token
}

@ApplicationScoped
class HypersysServiceBean(
    val hypersysProxy: HypersysProxy,
    val hypersysSystemTokenVerifier: HypersysSystemTokenVerifier,
    val hypersysLoginBean: HypersysLoginBean
) : HypersysService {

    @Inject
    lateinit var jwt: JsonWebToken

    override fun getAlleLokallag(): List<Organisasjonsledd> =
        hypersysProxy.get("/org/api/", getSystemToken(), ListOrganisasjonsleddTypeReference())

    override fun getAlleOrganPaaLaagasteNivaa(): List<SingleOrgan> = getAlleLokallag().map { toSingleOrgans(it) }.flatten()

    override fun login(loginRequest: LoginRequest): Token = hypersysLoginBean.login(loginRequest)

    private fun toSingleOrgans(lokallag: Organisasjonsledd): List<SingleOrgan> {
        // TODO: Denne må forbetrast. Tar berre med under-under, men vil at denne skal ta med alle som ikkje har organ under seg
        val organs: Organs = hypersysProxy.get("org/api/${lokallag.id}/organ/", getSystemToken(), Organs::class.java)
        return organs.organs.map {
            hypersysProxy.get("org/api/${lokallag.id}/organ/${it.id}/", getSystemToken(), SingleOrgan::class.java)
        }
    }

    private fun getSystemToken() = hypersysSystemTokenVerifier.assertGyldigSystemToken()
}