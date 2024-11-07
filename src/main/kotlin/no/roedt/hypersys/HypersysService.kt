package no.roedt.hypersys

import jakarta.enterprise.context.ApplicationScoped
import no.roedt.hypersys.externalModel.IsMember
import no.roedt.hypersys.externalModel.Organisasjonsledd
import no.roedt.hypersys.externalModel.membership.Membership
import no.roedt.hypersys.konvertering.ModelConverter
import no.roedt.hypersys.restClient.HypersysRestClient
import no.roedt.lokallag.Lokallag
import no.roedt.lokallag.LokallagService
import no.roedt.person.Person
import no.roedt.person.PersonService
import no.roedt.person.UserId
import java.time.Instant

@ApplicationScoped
class HypersysService(
    val hypersysRestClient: HypersysRestClient,
    val hypersysSystemTokenVerifier: HypersysSystemTokenVerifier,
    val personService: PersonService,
    val modelConverter: ModelConverter,
    val lokallagService: LokallagService
) {
    private fun getMedlemmer(hypersysLokallagId: Int?): List<Membership> =
        if (hypersysLokallagId == null) {
            listOf()
        } else {
            hypersysRestClient.hentMedlemmerILag(
                hypersysLokallagId = hypersysLokallagId,
                token = hypersysSystemTokenVerifier.assertGyldigSystemToken().access_token
            )
        }

    private fun convertToHypersysLokallagId(lokallag: Int): Int? {
        if (lokallag == -1) return null
        val hypersysId =
            lokallagService.findById(lokallag)
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
        personService.finnFraHypersysId(userId.userId).firstResult<Person>().lokallag

    private fun getLokallagIdFromHypersys(mittLag: Lokallag) =
        getAlleLokallag().first { mittLag.navn == it.name }
            .also { lokallagService.oppdater(it.id, it.name, mittLag.id) }
            .id

    fun oppdaterLokallag() {
        var lokallagAaLeggeTil: Set<Lokallag> = setOf()
        getAlleLokallag().forEach { lag ->
            if (lokallagService.exists("hypersysID", lag.id)) {
                lokallagService.oppdaterNavn(lag.id, lag.name)
            } else if (lokallagService.exists("navn", lag.name)) {
                lokallagService.oppdaterHypersysID(lag.id, lag.name)
            } else {
                lokallagAaLeggeTil =
                    lokallagAaLeggeTil.plus(
                        Lokallag(
                            navn = lag.name,
                            hypersysID = lag.id,
                            fylke = -1,
                            // TODO: har ikkje funne nokon god måte for å finne koplinga til fylke automatisk
                            sistOppdatert = Instant.now()
                        )
                    )
            }
        }
        lokallagAaLeggeTil.forEach { lokallagService.persist(it) }
    }

    fun getAlleLokallag(): List<Organisasjonsledd> = hypersysRestClient.hentAlleLokallag(hypersysSystemTokenVerifier.assertGyldigSystemToken().access_token)

    fun hentFraMedlemslista(hypersysID: Int?): Membership? =
        hypersysID
            ?.let { UserId(userId = it) }
            ?.let { getLokallag(userId = it) }
            ?.let { convertToHypersysLokallagId(it) }
            ?.let { getMedlemmer(it) }
            ?.firstOrNull { it.member_id == hypersysID }

    fun hentOppdatertMedlemslisteForLokallag(lokallag: Lokallag): Collection<Membership> =
        getMedlemmer(if (lokallag.hypersysID != null) lokallag.hypersysID else convertToHypersysLokallagId(lokallag.id))

    fun convertMembershipToPerson(membership: Membership) = modelConverter.convertMembershipToPerson(membership)

    fun konverterTilOppdatering(
        medlemskap: Membership,
        lokallag: Lokallag,
        person: Person
    ) = modelConverter.konverterTilOppdatering(medlemskap, lokallag, person)

    fun hentPerson(it: Person): IsMember = hypersysRestClient.hentPerson(
        it.hypersysID ?: throw IllegalArgumentException("Person ${it.id} mangler hypersys-ID"),
        hypersysSystemTokenVerifier.assertGyldigSystemToken().access_token)
}
