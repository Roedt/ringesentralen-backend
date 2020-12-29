package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.Brukarinformasjon
import no.roedt.ringesentralen.PersonRepository
import no.roedt.ringesentralen.hypersys.externalModel.Profile
import org.apache.http.entity.StringEntity
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.Dependent
import javax.persistence.EntityManager

@Dependent
class HypersysLoginBean(
        private val personRepository: PersonRepository,
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
        if (!isRegistered(loginRequest)) {
            register(loginRequest, gyldigToken)
        }
        return gyldigToken
    }

    private fun isRegistered(loginRequest: LoginRequest): Boolean = personRepository.find("email", loginRequest.brukarnamn).count() > 0L

    private fun register(loginRequest: LoginRequest, token: GyldigToken) {
        val profile: Profile = hypersysProxy.readResponse(hypersysProxy.gjennomfoerGetkall("actor/api/profile/", token))
        val brukarinformasjon: Brukarinformasjon = modelConverter.convert(profile)

        // TODO: Vurder kor mykje av dette som no bør lagrast i systemet, og kva som bør hentast ved behov
        // Eventuelt om vi skal hente dette frå hypersys ved kvar innlogging, og så mellomlagre?
        // Sånn at ved første gongs innlogging blir infoen registrert, og ved andre gongs innlogging og utover oppdatert
        // Bør kunne funke
        val sql = "CALL sp_registrerNyBruker(" +
                    "'${brukarinformasjon.fornamn}'," +
                    "'${brukarinformasjon.etternamn}', " +
                    "'${brukarinformasjon.telefonnummer.nummer}'," +
                    "'${brukarinformasjon.epost}'," +
                    "${brukarinformasjon.postnummer.postnummer}," +
                    "${brukarinformasjon.fylke.ordinal}," +
                    "'tullepassord'" +
                ")"
        val resultList = entityManager.createNativeQuery(sql).resultList
    }
}