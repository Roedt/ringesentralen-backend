package no.roedt.hypersys.konvertering

import no.roedt.Kilde
import no.roedt.brukere.FylkeRepository
import no.roedt.hypersys.externalModel.User
import no.roedt.hypersys.externalModel.membership.Membership
import no.roedt.lokallag.Lokallag
import no.roedt.person.Person
import no.roedt.person.PersonOppdatering
import no.roedt.person.PersonRepository
import no.roedt.ringesentralen.brukere.RingesentralenGroupID
import java.time.Instant
import javax.enterprise.context.Dependent

interface ModelConverter {
    fun convert(user: User, groupID: Int): Person
    fun convertMembershipToPerson(medlemskap: Membership): Person
    fun finnPostnummer(medlemskap: Membership): Int
    fun konverterTilOppdatering(medlemskap: Membership, lokallag: Lokallag, person: Person): PersonOppdatering
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

    override fun convertMembershipToPerson(medlemskap: Membership): Person {
        val postnummer = finnPostnummer(medlemskap)

        val telefonnummer = itOrNull(medlemskap.mobile)?.let { toTelefonnummer(it) }
        val groupID = personRepository.find("telefonnummer", telefonnummer)
            .singleResultOptional<Person>()
            .map { it.groupID() }
            .orElse(RingesentralenGroupID.KlarTilAaRinges.nr)
        // TODO: Finn på noko lurt her

        return Person(
            hypersysID = medlemskap.member_id.toString().toInt(),
            fornavn = medlemskap.first_name,
            etternavn = medlemskap.last_name,
            telefonnummer = telefonnummer,
            email = itOrNull(medlemskap.email),
            postnummer = postnummer,
            fylke = fylkeRepository.toFylke(postnummer),
            groupID = groupID,
            lokallag = lokallagConverter.tilLokallag(medlemskap.organisation),
            kilde = Kilde.Hypersys,
            sistOppdatert = Instant.now()
        )
    }

    override fun konverterTilOppdatering(
        medlemskap: Membership,
        lokallag: Lokallag,
        person: Person
    ): PersonOppdatering {
        val postnummer = finnPostnummer(medlemskap)
        return PersonOppdatering(
            hypersysID = medlemskap.member_id,
            fornavn = medlemskap.first_name,
            etternavn = medlemskap.last_name,
            telefonnummer = itOrNull(medlemskap.mobile)?.let { toTelefonnummer(it) },
            email = itOrNull(medlemskap.email),
            postnummer = postnummer,
            fylke = fylkeRepository.toFylke(postnummer),
            lokallag = lokallag.id,
            sistOppdatert = Instant.now(),
            groupID = person.groupID(),
            kilde = person.kilde
        )
    }

    override fun finnPostnummer(medlemskap: Membership): Int =
        medlemskap.postal_address
            .takeIf { it?.country?.equals("Norway") ?: false }
            ?.postal_code
            ?.let { if (it != "null") it else null }
            ?.toInt()
            ?: -1

    fun toTelefonnummer(telefonnummer: String): String? =
        telefonnummer.replace(" ", "").takeIf { it != "" }

    private fun toPostnummer(user: User): Int =
        user.addresses.map { it.postalCode }.map { it[1] }.map { it.toInt() }.maxByOrNull { it != 1 } ?: -1

    private fun itOrNull(any: Any?): String? = if (any.toString() != "") any.toString() else null
}
