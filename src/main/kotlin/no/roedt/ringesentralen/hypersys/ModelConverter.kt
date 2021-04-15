package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Kilde
import no.roedt.ringesentralen.brukere.FylkeRepository
import no.roedt.ringesentralen.hypersys.externalModel.Address
import no.roedt.ringesentralen.hypersys.externalModel.Membership
import no.roedt.ringesentralen.hypersys.externalModel.User
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import javax.enterprise.context.Dependent

interface ModelConverter {
    fun convert(user: User, groupID: Int) : Person
    fun convertMembershipToPerson(map: Map<*, *>): Person
    fun toFylke(postnummer: Int): Int
    fun finnPostnummer(map: Map<*, *>): Int
}

@Dependent
class ModelConverterBean(
    private val databaseUpdater: DatabaseUpdater,
    private val lokallagRepository: LokallagRepository,
    private val personRepository: PersonRepository,
    private val fylkeRepository: FylkeRepository
) : ModelConverter {

    override fun convert(user: User, groupID: Int): Person {
        val sisteMellomrom = user.name.lastIndexOf(" ")
        val fornavn = user.name.substring(0, sisteMellomrom)
        val etternavn = user.name.substring(sisteMellomrom+1)
        val postnummer = toPostnummer(user)
        val lokallag = toLokallag(user.memberships)
        val fylke = if (postnummer == -1 && lokallag != -1) fylkeRepository.getFylkeFraLokallag(lokallag) else toFylke(postnummer)
        return Person(
            hypersysID = user.id,
            fornavn = fornavn,
            etternavn = etternavn,
            email = user.email,
            telefonnummer = toTelefonnummer(if (user.phone != "") user.phone else user.phone2),
            postnummer = postnummer,
            fylke = fylke,
            lokallag = lokallag,
            groupID = groupID,
            kilde = Kilde.Hypersys
        )
    }

    override fun convertMembershipToPerson(map: Map<*, *>) : Person {
        val postnummer = finnPostnummer(map)

        val telefonnummer = itOrNull(map["mobile"])?.let { toTelefonnummer(it) }
        val groupID = personRepository.find("telefonnummer", telefonnummer)
            .singleResultOptional<Person>()
            .map { it.groupID }
            .orElse(GroupID.KlarTilAaRinges.nr)


        return Person(
            hypersysID = map["member_id"].toString().toInt(),
            fornavn = map["first_name"].toString(),
            etternavn = map["last_name"].toString(),
            telefonnummer = telefonnummer,
            email = itOrNull(map["email"]),
            postnummer = postnummer,
            fylke = toFylke(postnummer),
            groupID = groupID,
            lokallag = lokallagRepository.fromOrganisationName(map["organisation"].toString()),
            kilde = Kilde.Hypersys
        )
    }

    override fun finnPostnummer(map: Map<*, *>): Int {
        val addresses = listOf(toAddress(map["postal_address"]))
        return addresses.flatMap { it.postalCode }.firstOrNull { i -> i != "null" }?.toString()?.toInt() ?: -1
    }

    private fun toAddress(get: Any?): Address {
        val address = get as Map<*, *>
        return Address(
            id = 1,
            name = address["address1"].toString(),
            address1 = address["address1"].toString(),
            address2 = address["address2"].toString(),
            subject = "",
            postalCode = listOf(address["postal_code"].toString())
        )
    }

    fun toTelefonnummer(telefonnummer: String): String? =
        telefonnummer.replace(" ", "").takeIf { it != "" }


    private fun toPostnummer(user: User) : Int {
        val postnummer = user.addresses.map { it.postalCode }.map { it[1] }.map { it.toInt() }.maxByOrNull { it != 1 } ?: -1
        if (postnummer == -1) {
            println("Fekk postnummer -1, dette var brukaren: $user")
        }
        return postnummer
    }

    override fun toFylke(postnummer: Int): Int =
        databaseUpdater.getResultList(
            "select fylke.id from `postnummer` p " +
                    "inner join kommune kommune on p.KommuneKode = kommune.nummer " +
                    "inner join `fylker` fylke on fylke.id=kommune.fylke_id where postnummer = $postnummer"
        )
            .map { it as Int }
            .firstOrNull()
            ?: -1

    fun toLokallag(memberships: List<Membership>): Int = getOrganisationName(memberships)?.let { lokallagRepository.fromOrganisationName(it) } ?: -1

    private fun getOrganisationName(memberships: List<Membership>) =
        memberships
            .asSequence()
            .sortedByDescending { it.startDate }
            .map { it.organisationName }
            .firstOrNull()

    private fun itOrNull(any: Any?): String? = if (any.toString() != "") any.toString() else null
}