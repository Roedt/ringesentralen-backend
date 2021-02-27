package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.hypersys.externalModel.Organisasjonsledd
import no.roedt.ringesentralen.hypersys.externalModel.Organs
import no.roedt.ringesentralen.hypersys.externalModel.SingleOrgan
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import org.eclipse.microprofile.jwt.JsonWebToken
import javax.enterprise.context.ApplicationScoped

interface HypersysService {
    fun getAlleLokallag(): List<Organisasjonsledd>
    fun getAlleOrganPaaLaagasteNivaa(): List<SingleOrgan>
    fun login(loginRequest: LoginRequest): Token
    fun getMedlemmer(userId: UserId, token: JsonWebToken): List<LinkedHashMap<String, *>>
}

@ApplicationScoped
class HypersysServiceBean(
    val hypersysProxy: HypersysProxy,
    val hypersysSystemTokenVerifier: HypersysSystemTokenVerifier,
    val hypersysLoginBean: HypersysLoginBean,
    val personRepository: PersonRepository,
    val lokallagRepository: LokallagRepository
) : HypersysService {

    override fun getAlleLokallag(): List<Organisasjonsledd> =
        hypersysProxy.get("/org/api/", getSystemToken(), ListOrganisasjonsleddTypeReference())

    override fun getAlleOrganPaaLaagasteNivaa(): List<SingleOrgan> = getAlleLokallag().map { toSingleOrgans(it) }.flatten()

    override fun login(loginRequest: LoginRequest): Token = hypersysLoginBean.login(loginRequest)

    override fun getMedlemmer(userId: UserId, token: JsonWebToken): List<LinkedHashMap<String, *>> =
        getMedlemmar(userId,
            GyldigPersonToken(
                access_token = token.claim<String>("hypersys.access_token").get(),
                expires_in = token.claim<Any>("hypersys.expires_in").get().toString().toInt(),
                token_type = token.claim<String>("hypersys.token_type").get(),
                scope = token.claim<String>("hypersys.scope").get(),
                refresh_token = token.claim<String>("hypersys.refresh_token").get(),
                user_id = token.claim<String>("hypersys.user_id").get()
            )
        )

    private fun getMedlemmar(userId: UserId, token: GyldigPersonToken) =
        hypersysProxy.get("/membership/api/membership/${getLokallag(userId)}/2021/", token, List::class.java) as List<LinkedHashMap<String, *>>

    private fun getLokallag(userId: UserId) = personRepository.find("hypersysID", userId.userId).firstResult<Person>().lokallag
        .let { lokallagRepository.findById(it.toLong())}
        .let { it.navn }
        .let { mittLag -> getAlleLokallag().first { mittLag == it.name } }
        .let { it.id }

    private fun toSingleOrgans(lokallag: Organisasjonsledd): List<SingleOrgan> {
        // TODO: Denne må forbetrast. Tar berre med under-under, men vil at denne skal ta med alle som ikkje har organ under seg
        val organs: Organs = hypersysProxy.get("org/api/${lokallag.id}/organ/", getSystemToken(), Organs::class.java)
        return organs.organs.map {
            hypersysProxy.get("org/api/${lokallag.id}/organ/${it.id}/", getSystemToken(), SingleOrgan::class.java)
        }
    }

    private fun getSystemToken() = hypersysSystemTokenVerifier.assertGyldigSystemToken()
}