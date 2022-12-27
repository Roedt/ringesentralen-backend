package no.roedt.hypersys

import no.roedt.hypersys.externalModel.Organisasjonsledd
import no.roedt.lokallag.Lokallag
import no.roedt.lokallag.LokallagRepository
import no.roedt.person.Person
import no.roedt.person.PersonRepository
import no.roedt.person.UserId
import java.time.Instant
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped

interface HypersysService {
    fun getMedlemmer(hypersysLokallagId: Int?): List<LinkedHashMap<String, *>>
    fun convertToHypersysLokallagId(lokallag: Int): Int?
    fun hentFraMedlemslista(hypersysID: Int?): LinkedHashMap<*, *>?
    fun oppdaterMedlemmerILokallag(lokallag: Lokallag)
    fun oppdaterLokallag()
}

@ApplicationScoped
class HypersysServiceBean(
    val hypersysProxy: HypersysProxy,
    val hypersysSystemTokenVerifier: HypersysSystemTokenVerifier,
    val personRepository: PersonRepository,
    private val modelConverter: ModelConverter,
    val lokallagRepository: LokallagRepository
) : HypersysService {

    override fun getMedlemmer(hypersysLokallagId: Int?): List<LinkedHashMap<String, *>> {
        return if (hypersysLokallagId == null) {
            listOf()
        } else {
            hypersysProxy.get(
                "/membership/api/membership/$hypersysLokallagId/${LocalDate.now().year}/",
                hypersysSystemTokenVerifier.assertGyldigSystemToken(),
                List::class.java
            )
                as List<LinkedHashMap<String, *>>
        }
    }

    override fun convertToHypersysLokallagId(lokallag: Int): Int? {
        if (lokallag == -1) return null
        val hypersysId = lokallagRepository.findById(lokallag)
            ?.let { mittLag ->
                if (mittLag.hypersysID != null) {
                    mittLag.hypersysID!!
                } else {
                    getLokallagIdFromHypersys(
                        mittLag
                    )
                }
            }
        if (hypersysId == null) println("Fann ikkje lokallag i hypersys for $lokallag")
        return hypersysId
    }

    private fun getLokallag(userId: UserId) =
        personRepository.find("hypersysID", userId.userId).firstResult<Person>().lokallag

    private fun getLokallagIdFromHypersys(mittLag: Lokallag): Int {
        val alleLokallag = getAlleLokallag()
        val lag = alleLokallag.first { mittLag.navn == it.name }
        lokallagRepository.update("hypersysID=?1, navn=?2 where id=?3", lag.id, lag.name, mittLag.id)
        return lag.id
    }

    override fun oppdaterLokallag() {
        var lokallagAaLeggeTil: Set<Lokallag> = setOf()
        val alleLokallag = getAlleLokallag()
        alleLokallag.forEach { lag ->
            if (lokallagRepository.find("hypersysID", lag.id).count() > 0) {
                lokallagRepository.update("navn=?1 where hypersysID=?2", lag.name, lag.id)
            } else if (lokallagRepository.find("navn", lag.name).count() > 0) {
                lokallagRepository.update("hypersysID=?1 where navn=?2", lag.id, lag.name)
            } else {
                lokallagAaLeggeTil = lokallagAaLeggeTil.plus(
                    Lokallag(
                        navn = lag.name,
                        hypersysID = lag.id,
                        fylke = -1, // TODO: har ikkje funne nokon god måte for å finne koplinga til fylke automatisk
                        sistOppdatert = Instant.now()
                    )
                )
            }
        }
        lokallagAaLeggeTil.forEach { lokallagRepository.persist(it) }
    }

    private fun getAlleLokallag(): List<Organisasjonsledd> =
        hypersysProxy.get(
            "/org/api/",
            hypersysSystemTokenVerifier.assertGyldigSystemToken(),
            ListOrganisasjonsleddTypeReference()
        )

    override fun oppdaterMedlemmerILokallag(lokallag: Lokallag) {
        val hypersysLokallagId =
            if (lokallag.hypersysID != null) lokallag.hypersysID else convertToHypersysLokallagId(lokallag.id)
        getMedlemmer(hypersysLokallagId)
            .filter { medlem -> personRepository.find("hypersysID", medlem["member_id"]).count() == 0L }
            .map { modelConverter.convertMembershipToPerson(it) }
            .filter { it.telefonnummer != null }
            .forEach { personRepository.save(it) }
        // TODO: Mekanisme ca her for å slette dei som ikkje lenger er med i laget
        // Eventuelt noko lurt for å anonymisere eller noko
        // Kanskje vi også eksplisitt skal sjekke dei mot HS for å sjå om dei berre har bytta lag
    }

    override fun hentFraMedlemslista(hypersysID: Int?): LinkedHashMap<*, *>? =
        hypersysID
            ?.let { UserId(userId = it) }
            ?.let { getLokallag(userId = it) }
            ?.let { convertToHypersysLokallagId(it) }
            ?.let { getMedlemmer(it) }
            ?.firstOrNull { it["member_id"] == hypersysID }
}
