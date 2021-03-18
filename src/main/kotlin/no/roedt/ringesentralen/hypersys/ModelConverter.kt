package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.hypersys.externalModel.Address
import no.roedt.ringesentralen.hypersys.externalModel.Membership
import no.roedt.ringesentralen.hypersys.externalModel.User
import no.roedt.ringesentralen.lokallag.Lokallag
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.Person
import javax.enterprise.context.Dependent

interface ModelConverter {
    fun convert(user: User, groupID: Int) : Person
    fun convertMembershipToPerson(map: Map<*, *>): Person
}

@Dependent
class ModelConverterBean(
    private val databaseUpdater: DatabaseUpdater,
    private val lokallagRepository: LokallagRepository
) : ModelConverter {

    override fun convert(user: User, groupID: Int): Person {
        val sisteMellomrom = user.name.lastIndexOf(" ")
        val fornavn = user.name.substring(0, sisteMellomrom)
        val etternavn = user.name.substring(sisteMellomrom+1)
        val postnummer = toPostnummer(user)
        return Person(
            hypersysID = user.id,
            fornavn = fornavn,
            etternavn = etternavn,
            email = user.email,
            telefonnummer = toTelefonnummer(user.phone),
            postnummer = postnummer,
            fylke = toFylke(postnummer),
            lokallag =  toLokallag(user.memberships) ?: -1,
            groupID = groupID
        )
    }


    override fun convertMembershipToPerson(map: Map<*, *>) : Person {
        val postnummer = finnPostnummer(map)

        return Person(
            hypersysID = map["member_id"].toString().toInt(),
            fornavn = map["first_name"].toString(),
            etternavn = map["last_name"].toString(),
            telefonnummer = itOrNull(map["mobile"]),
            email = itOrNull(map["email"]),
            postnummer = postnummer,
            fylke = toFylke(postnummer),
            groupID = 1,
            lokallag = toLokallag(map["organisation"].toString())
        )
    }
    private fun finnPostnummer(map: Map<*, *>): Int {
        val addresses = listOf(toAddress(map["postal_address"]))
        return addresses.flatMap { it.postalCode }.firstOrNull { i -> i != "null" }?.toString()?.toInt() ?: -1
    }

    private fun toAddress(get: Any?): Address {
        val address = get as Map<String, String>
        return Address(
            id = 1,
            name = address["address1"].toString(),
            address1 = address["address1"].toString(),
            address2 = address["address2"].toString(),
            subject = "",
            postalCode = listOf(address["postal_code"].toString())
        )
    }


    fun toTelefonnummer(telefonnummer: String): String? {
        val splitted = telefonnummer.split(" ")
        return when {
            splitted.size >= 2 -> splitted[1]
            else -> null
        }
    }
    private fun toPostnummer(user: User) : Int = user.addresses.map { it.postalCode }.map{ it[1] }.map{ it.toInt() }.firstOrNull() ?: 1

    private fun toFylke(postnummer: Int): Int =
        databaseUpdater.getResultList(
            "select fylke.id from `postnummer` p " +
                    "inner join kommune kommune on p.KommuneKode = kommune.nummer " +
                    "inner join `fylker` fylke on fylke.id=kommune.fylke_id where postnummer = $postnummer"
        )
            .map { it as Int }
            .firstOrNull()
            ?: -1


    fun toLokallag(memberships: List<Membership>): Int = getOrganisationName(memberships)?.let { toLokallag(it) } ?: -1

    fun toLokallag(organisationName: String) : Int =
        organisationName
            .let { lokallagRepository.find("navn", it) }
            .firstResultOptional<Lokallag>()
            ?.map { it.id }
            ?.map { it.toInt() }
            ?.orElse(-1)
            ?: -1

    private fun getOrganisationName(memberships: List<Membership>) =
        memberships
            .asSequence()
            .sortedByDescending { it.startDate }
            .map { it.organisationName }
            .firstOrNull()

    private fun itOrNull(any: Any?): String? = if (any.toString() != "") any.toString() else null
}