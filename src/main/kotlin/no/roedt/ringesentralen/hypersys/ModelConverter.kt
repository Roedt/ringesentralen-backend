package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.*
import no.roedt.ringesentralen.hypersys.externalModel.Membership
import no.roedt.ringesentralen.hypersys.externalModel.Profile
import no.roedt.ringesentralen.hypersys.externalModel.User
import javax.enterprise.context.Dependent
import javax.persistence.EntityManager

@Dependent
class ModelConverter(
    private val entityManager: EntityManager,
    private val lokallagRepository: LokallagRepository
) {
    fun convert(profile: Profile) : Brukarinformasjon = convert(profile.user)

    private fun convert(user: User): Brukarinformasjon {
        val sisteMellomrom = user.name.lastIndexOf(" ")
        val fornamn = user.name.substring(0, sisteMellomrom)
        val etternamn = user.name.substring(sisteMellomrom+1)
        val postnummer = toPostnummer(user)
        return Brukarinformasjon(
            hypersysID = user.id,
            fornamn = fornamn,
            etternamn = etternamn,
            epost = user.email,
            telefonnummer = toTelefonnummer(user.phone),
            postnummer = postnummer,
            fylke = toFylke(postnummer),
            lokallag = toLokallag(user.memberships)
        )
    }

    private fun toTelefonnummer(phone: String): Telefonnummer? {
        val splitted = phone.split(" ")

        return when {
            splitted.size >= 2 -> Telefonnummer(landkode = splitted[0], nummer = Integer.parseInt(splitted[1]))
            else -> null
        }
    }

    private fun toPostnummer(user: User) = user.addresses.map { it.postalCode }.map{ it[1] }.map { Postnummer(it) }.firstOrNull() ?: Postnummer("0000")

    private fun toFylke(postnummer: Postnummer): Fylke =
        entityManager.createNativeQuery("select countyID from `postnumber` where postnumber = ${postnummer.getPostnummer()}").resultList.first().let { Fylke.from(it as Int) }

    private fun toLokallag(memberships: List<Membership>): Lokallag? =
        memberships
            .map { it.organisationName }
            .map { lokallagRepository.find("name", it) }
            .firstOrNull()
            ?.firstResult()
}