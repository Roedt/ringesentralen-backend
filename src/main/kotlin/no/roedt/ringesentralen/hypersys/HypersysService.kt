package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.hypersys.externalModel.Organisasjonsledd
import no.roedt.ringesentralen.lokallag.Lokallag
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import org.eclipse.microprofile.jwt.JsonWebToken
import javax.enterprise.context.ApplicationScoped

interface HypersysService {
    fun getAlleLokallag(): List<Organisasjonsledd>
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
        hypersysProxy.get("/org/api/", hypersysSystemTokenVerifier.assertGyldigSystemToken(), ListOrganisasjonsleddTypeReference())

    override fun login(loginRequest: LoginRequest): Token = hypersysLoginBean.login(loginRequest)

    override fun getMedlemmer(userId: UserId, token: JsonWebToken): List<LinkedHashMap<String, *>> =
        hypersysProxy.get(
            "/membership/api/membership/${getLokallag(userId)}/2021/",
            GyldigPersonToken.from(token),
            List::class.java)
                as List<LinkedHashMap<String, *>>

    private fun getLokallag(userId: UserId) = personRepository.find("hypersysID", userId.userId).firstResult<Person>().lokallag
        .let { lokallagRepository.findById(it.toLong())}
        .let { mittLag -> if (mittLag.hypersysID != null) mittLag.hypersysID else getLokallagIdFromHypersys(mittLag) }

    private fun getLokallagIdFromHypersys(mittLag: Lokallag) : Int {
        val lag = getAlleLokallag().first { mittLag.navn == it.name }
        lokallagRepository.update("hypersysID=?1 where id=?2", lag.id, mittLag.id)
        return lag.id
    }
}