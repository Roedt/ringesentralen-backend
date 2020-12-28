package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.PersonRepository
import no.roedt.ringesentralen.brukere.Brukerendring
import no.roedt.ringesentralen.brukere.TilgangsendringsRequest
import no.roedt.ringesentralen.samtale.GroupID
import org.apache.http.entity.StringEntity
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.Dependent
import javax.inject.Inject
import javax.persistence.EntityManager

@Dependent
class HypersysLoginBean(
        private val personRepository: PersonRepository,
        private val hypersysProxy: HypersysProxy,
        private val entityManager: EntityManager
) {

    @ConfigProperty(name = "brukarId")
    lateinit var brukarId: String

    @ConfigProperty(name = "brukarSecret")
    lateinit var brukarSecret: String

    fun login(loginRequest: LoginRequest): Token {
        val httpPost = hypersysProxy.createHttpPostWithHeader(brukarId, brukarSecret)
        httpPost.entity = StringEntity("grant_type=password&username=${loginRequest.brukarnamn}&password=${loginRequest.passord}")
        val response = hypersysProxy.httpCall(httpPost)
        val token = hypersysProxy.readResponse<Token>(response)
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
        val brukarinformasjon: Brukarinformasjon =
                hypersysProxy.readResponse(hypersysProxy.gjennomfoerGetkall(
                        "url til info om personen + ${loginRequest.brukarnamn}", token))
        entityManager.createNativeQuery(
                "CALL sp_registrerNyBruker(" +
                        "${brukarinformasjon.fornamn}," +
                        "${brukarinformasjon.etternamn}," +
                        "${brukarinformasjon.telefonnummer}," +
                        "${brukarinformasjon.epost}," +
                        "${brukarinformasjon.postnummer}," +
                        "${brukarinformasjon.fylke})").resultList
    }
}