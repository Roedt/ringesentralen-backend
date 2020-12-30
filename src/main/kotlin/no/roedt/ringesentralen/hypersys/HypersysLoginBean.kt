package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.Brukarinformasjon
import no.roedt.ringesentralen.hypersys.externalModel.Profile
import org.apache.http.entity.StringEntity
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.Dependent
import javax.persistence.EntityManager

@Dependent
class HypersysLoginBean(
        private val hypersysProxy: HypersysProxy,
        private val entityManager: EntityManager,
        private val modelConverter: ModelConverter
) {

    @ConfigProperty(name = "brukarId")
    lateinit var brukarId: String

    @ConfigProperty(name = "brukarSecret")
    lateinit var brukarSecret: String

    fun login(loginRequest: LoginRequest): Token {
        val httpPost = hypersysProxy.createHttpPostWithHeader(brukarId, brukarSecret)
        httpPost.entity = StringEntity("grant_type=password&username=${loginRequest.brukarnamn}&password=${loginRequest.passord}")
        val response = hypersysProxy.httpCall(httpPost)
        if (response?.statusLine?.statusCode != 200) {
            return hypersysProxy.readResponse<UgyldigToken>(response)
        }

        val gyldigToken = hypersysProxy.readResponse<GyldigToken>(response)
        oppdaterRingerFraaHypersys(gyldigToken)
        return gyldigToken
    }

    private fun oppdaterRingerFraaHypersys(token: GyldigToken) {
        val profile: Profile = hypersysProxy.readResponse(hypersysProxy.gjennomfoerGetkall("actor/api/profile/", token))
        val brukarinformasjon: Brukarinformasjon = modelConverter.convert(profile)

        entityManager.createNativeQuery(brukarinformasjon.toSQL()).resultList
    }

    fun Brukarinformasjon.toSQL(): String = "CALL sp_registrerNyBruker(" +
            "'${fornamn}'," +
            "'${etternamn}', " +
            "'${telefonnummer.nummer}'," +
            "'${epost}'," +
            "${postnummer.postnummer}," +
            "${fylke.nr}" +
            ")"
}