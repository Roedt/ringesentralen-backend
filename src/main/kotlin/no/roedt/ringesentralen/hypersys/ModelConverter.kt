package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.*
import no.roedt.ringesentralen.hypersys.externalModel.Membership
import no.roedt.ringesentralen.hypersys.externalModel.Profile
import no.roedt.ringesentralen.hypersys.externalModel.User
import javax.enterprise.context.Dependent
import javax.persistence.EntityManager

interface ModelConverter {
    fun convert(profile: Profile): Brukerinformasjon
}

@Dependent
class ModelConverterBean(
    private val entityManager: EntityManager,
    private val fylkeRepository: FylkeRepository,
    private val lokallagRepository: LokallagRepository
) : ModelConverter {
    override fun convert(profile: Profile) : Brukerinformasjon = convert(profile.user)

    private fun convert(user: User): Brukerinformasjon {
        val sisteMellomrom = user.name.lastIndexOf(" ")
        val fornamn = user.name.substring(0, sisteMellomrom)
        val etternamn = user.name.substring(sisteMellomrom+1)
        val postnummer = toPostnummer(user)
        return Brukerinformasjon(
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

    fun toTelefonnummer(telefonnummer: String): Telefonnummer? {
        val splitted = telefonnummer.split(" ")
        return when {
            splitted.size >= 2 -> Telefonnummer(landkode = splitted[0], nummer = Integer.parseInt(splitted[1]))
            else -> null
        }
    }

    fun toPostnummer(user: User) = user.addresses.map { it.postalCode }.map{ it[1] }.map { Postnummer(it) }.firstOrNull() ?: Postnummer("0000")

    //TODO fix
    private fun toFylke(postnummer: Postnummer): Fylke =
        entityManager.createNativeQuery(
            "select fylke.id from `postnummer` p " +
                    "inner join kommune kommune on p.KommuneKode = kommune.nummer " +
                    "inner join `fylker` fylke on fylke.id=kommune.fylke_id where postnummer = ${postnummer.getPostnummer()}"
        )
            .resultList
            .map { it as Int }
            .map { fylkeRepository.findById(it) }
            .first()


    fun toLokallag(memberships: List<Membership>): Lokallag? =
        memberships
            .sortedByDescending { it.startDate }
            .map { it.organisationName }
            .map { lokallagRepository.find("navn", it) }
            .firstOrNull()
            ?.firstResult()
}