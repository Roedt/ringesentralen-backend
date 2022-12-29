package no.roedt.hypersys.konvertering

import no.roedt.Kilde
import no.roedt.brukere.FylkeRepository
import no.roedt.hypersys.externalModel.User
import no.roedt.lokallag.Lokallag
import no.roedt.person.Person
import no.roedt.person.PersonOppdatering
import no.roedt.person.PersonRepository
import no.roedt.ringesentralen.brukere.RingesentralenGroupID
import java.time.Instant
import javax.enterprise.context.Dependent

interface ModelConverter {
    fun convert(user: User, groupID: Int): Person
    fun convertMembershipToPerson(map: Map<*, *>): Person
    fun toFylke(postnummer: Int): Int
    fun finnPostnummer(map: Map<*, *>): Int
    fun konverterTilOppdatering(map: Map<String, *>, lokallag: Lokallag, person: Person): PersonOppdatering
}

@Dependent
class ModelConverterBean(
    private val lokallagConverter: LokallagConverter,
    private val personRepository: PersonRepository,
    private val fylkeRepository: FylkeRepository
) : ModelConverter {

    override fun convert(user: User, groupID: Int): Person {
        val sisteMellomrom = user.name.lastIndexOf(" ")
        val fornavn = user.name.substring(0, sisteMellomrom)
        val etternavn = user.name.substring(sisteMellomrom + 1)
        val postnummer = toPostnummer(user)
        val lokallag = lokallagConverter.tilLokallag(user.memberships)
        val fylke = fylkeRepository.getFylke(lokallag, postnummer)
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
            kilde = Kilde.Hypersys,
            sistOppdatert = Instant.now()
        )
    }

    override fun convertMembershipToPerson(map: Map<*, *>): Person {
        val postnummer = finnPostnummer(map)

        val telefonnummer = itOrNull(map["mobile"])?.let { toTelefonnummer(it) }
        val groupID = personRepository.find("telefonnummer", telefonnummer)
            .singleResultOptional<Person>()
            .map { it.groupID() }
            .orElse(RingesentralenGroupID.KlarTilAaRinges.nr)
        // TODO: Finn på noko lurt her

        return Person(
            hypersysID = map["member_id"].toString().toInt(),
            fornavn = map["first_name"].toString(),
            etternavn = map["last_name"].toString(),
            telefonnummer = telefonnummer,
            email = itOrNull(map["email"]),
            postnummer = postnummer,
            fylke = fylkeRepository.toFylke(postnummer),
            groupID = groupID,
            lokallag = lokallagConverter.tilLokallag(map),
            kilde = Kilde.Hypersys,
            sistOppdatert = Instant.now()
        )
    }

    override fun konverterTilOppdatering(map: Map<String, *>, lokallag: Lokallag, person: Person): PersonOppdatering {
        val postnummer = finnPostnummer(map)
        return PersonOppdatering(
            hypersysID = map["member_id"].toString().toInt(),
            fornavn = map["first_name"].toString(),
            etternavn = map["last_name"].toString(),
            telefonnummer = itOrNull(map["mobile"])?.let { toTelefonnummer(it) },
            email = itOrNull(map["email"]),
            postnummer = postnummer,
            fylke = fylkeRepository.toFylke(postnummer),
            lokallag = lokallag.id,
            sistOppdatert = Instant.now(),
            groupID = person.groupID(),
            kilde = person.kilde
        )
    }

    override fun finnPostnummer(map: Map<*, *>): Int =
        (map["postal_address"] as Map<*, *>)
            .takeIf { it["country"]?.equals("Norway") ?: false }
            ?.get("postal_code")
            ?.toString()
            ?.let { listOf(it) }
            ?.firstOrNull { i -> i != "null" }
            ?.toString()
            ?.toInt()
            ?: -1

    fun toTelefonnummer(telefonnummer: String): String? =
        telefonnummer.replace(" ", "").takeIf { it != "" }

    private fun toPostnummer(user: User): Int =
        user.addresses.map { it.postalCode }.map { it[1] }.map { it.toInt() }.maxByOrNull { it != 1 } ?: -1

    override fun toFylke(postnummer: Int): Int = fylkeRepository.toFylke(postnummer)

    private fun itOrNull(any: Any?): String? = if (any.toString() != "") any.toString() else null
}
