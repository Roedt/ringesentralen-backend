package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.Brukarinformasjon
import no.roedt.ringesentralen.hypersys.externalModel.Profile
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
        val request = hypersysProxy.createHttpPostWithHeader(brukarId, brukarSecret, "grant_type=password&username=${loginRequest.brukarnamn}&password=${loginRequest.passord}")
        val response = hypersysProxy.httpCall(request)
        if (response.statusCode() != 200) {
            return hypersysProxy.readResponse(response, UgyldigToken::class.java)
        }

        val gyldigToken = hypersysProxy.readResponse(response, GyldigToken::class.java)
        oppdaterRingerFraaHypersys(gyldigToken)
        return gyldigToken
    }

    private fun oppdaterRingerFraaHypersys(token: GyldigToken) {
        val profile: Profile = hypersysProxy.readResponse(hypersysProxy.gjennomfoerGetkall("actor/api/profile/", token), Profile::class.java)
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