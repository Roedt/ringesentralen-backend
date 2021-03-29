package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.hypersys.externalModel.Organisasjonsledd
import no.roedt.ringesentralen.lokallag.Lokallag
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import javax.enterprise.context.ApplicationScoped

interface HypersysService {
    fun getMedlemmer(hypersysLokallagId: Int?): List<LinkedHashMap<String, *>>
    fun convertToHypersysLokallagId(lokallag: Int): Int?
    fun getLokallag(userId: UserId): Int?
}

@ApplicationScoped
class HypersysServiceBean(
    val hypersysProxy: HypersysProxy,
    val hypersysSystemTokenVerifier: HypersysSystemTokenVerifier,
    val personRepository: PersonRepository,
    val lokallagRepository: LokallagRepository
) : HypersysService {

    override fun getMedlemmer(hypersysLokallagId: Int?): List<LinkedHashMap<String, *>> {
        return if (hypersysLokallagId == null) listOf()
        else hypersysProxy.get("/membership/api/membership/$hypersysLokallagId/2021/", hypersysSystemTokenVerifier.assertGyldigSystemToken(), List::class.java)
                as List<LinkedHashMap<String, *>>
    }

    override fun convertToHypersysLokallagId(lokallag: Int) : Int? {
        val hypersysId = lokallagRepository.findById(lokallag.toLong())
            ?.let { mittLag ->
                if (mittLag.hypersysID != null) mittLag.hypersysID!! else getLokallagIdFromHypersys(
                    mittLag
                )
            }
        if (hypersysId == null) println("Fann ikkje lokallag i hypersys for $lokallag")
        return hypersysId
    }

    override fun getLokallag(userId: UserId) = personRepository.find("hypersysID", userId.userId).firstResult<Person>().lokallag

    private fun getLokallagIdFromHypersys(mittLag: Lokallag) : Int {
        val lag = getAlleLokallag().first { mittLag.navn == it.name }
        lokallagRepository.update("hypersysID=?1 where id=?2", lag.id, mittLag.id)
        return lag.id
    }

    private fun getAlleLokallag(): List<Organisasjonsledd> =
        hypersysProxy.get("/org/api/", hypersysSystemTokenVerifier.assertGyldigSystemToken(), ListOrganisasjonsleddTypeReference())
}