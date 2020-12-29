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
        val token = hypersysProxy.readResponse<GyldigToken>(response) // TODO: Typen her bør vera berre Token
        if (response?.statusLine?.statusCode != 200) {
            return token as UgyldigToken
        }

        val gyldigToken = token as GyldigToken
        oppdaterRingerFraaHypersys(gyldigToken)
        return gyldigToken
    }

    private fun oppdaterRingerFraaHypersys(token: GyldigToken) {
        val profile: Profile = hypersysProxy.readResponse(hypersysProxy.gjennomfoerGetkall("actor/api/profile/", token))
        val brukarinformasjon: Brukarinformasjon = modelConverter.convert(profile)

        // TODO: Vurder kor mykje av dette som no bør lagrast i systemet, og kva som bør hentast ved behov
        // Eventuelt om vi skal hente dette frå hypersys ved kvar innlogging, og så mellomlagre?
        // Sånn at ved første gongs innlogging blir infoen registrert, og ved andre gongs innlogging og utover oppdatert
        entityManager.createNativeQuery(brukarinformasjon.toSQL()).resultList
    }

    fun Brukarinformasjon.toSQL(): String = "CALL sp_registrerNyBruker(" +
            "'${fornamn}'," +
            "'${etternamn}', " +
            "'${telefonnummer.nummer}'," +
            "'${epost}'," +
            "${postnummer.postnummer}," +
            "${fylke.ordinal}" +
            ")"
}