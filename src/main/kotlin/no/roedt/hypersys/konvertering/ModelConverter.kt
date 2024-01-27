package no.roedt.hypersys.konvertering

import jakarta.enterprise.context.Dependent
import no.roedt.Kilde
import no.roedt.fylke.FylkeService
import no.roedt.hypersys.externalModel.User
import no.roedt.hypersys.externalModel.membership.Membership
import no.roedt.lokallag.Lokallag
import no.roedt.person.Oppdateringskilde
import no.roedt.person.Person
import no.roedt.person.PersonOppdatering
import no.roedt.person.PersonService
import no.roedt.postnummer.Postnummer
import no.roedt.postnummer.PostnummerService
import no.roedt.ringesentralen.brukere.RingesentralenGroupID
import java.time.Instant

interface ModelConverter {
    fun convert(
        user: User,
        groupID: Int
    ): Person

    fun convertMembershipToPerson(medlemskap: Membership): Person

    fun finnPostnummer(medlemskap: Membership): Postnummer

    fun konverterTilOppdatering(
        medlemskap: Membership,
        lokallag: Lokallag,
        person: Person
    ): PersonOppdatering
}

@Dependent
class ModelConverterBean(
    private val lokallagConverter: LokallagConverter,
    private val personService: PersonService,
    private val fylkeService: FylkeService,
    private val postnummerService: PostnummerService
) : ModelConverter {
    override fun convert(
        user: User,
        groupID: Int
    ): Person {
        val sisteMellomrom = user.name.lastIndexOf(" ")
        val fornavn = user.name.substring(0, sisteMellomrom)
        val etternavn = user.name.substring(sisteMellomrom + 1)
        val postnummer = toPostnummer(user)
        val lokallag = lokallagConverter.tilLokallag(user.memberships)
        val fylke = fylkeService.getFylke(lokallag, postnummer)
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
        val groupID =
            personService.finnFraTelefonnummer(telefonnummer)
                .map { it.groupID() }
                .orElse(RingesentralenGroupID.KlarTilAaRinges.nr)
        // TODO: Finn på noko lurt her

        return Person(
            hypersysID = medlemskap.member_id,
            fornavn = medlemskap.first_name.vask(),
            etternavn = medlemskap.last_name.vask(),
            telefonnummer = telefonnummer?.vask(),
            email = itOrNull(medlemskap.email)?.vask(),
            postnummer = postnummer,
            fylke = fylkeService.toFylke(postnummer),
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
            fornavn = medlemskap.first_name.vask(),
            etternavn = medlemskap.last_name.vask(),
            telefonnummer = itOrNull(medlemskap.mobile)?.let { toTelefonnummer(it) }?.vask(),
            email = itOrNull(medlemskap.email?.vask()),
            postnummer = postnummer,
            fylke = fylkeService.toFylke(postnummer),
            lokallag = lokallag.id,
            sistOppdatert = Instant.now(),
            groupID = person.groupID(),
            kilde = person.kilde,
            oppdateringskilde = Oppdateringskilde.Hypersys
        )
    }

    override fun finnPostnummer(medlemskap: Membership): Postnummer =
        medlemskap.postal_address
            .takeIf { it?.country?.equals("Norway") ?: false }
            ?.postal_code
            ?.let { if (it != "null") it else null }
            ?.let { postnummerService.findById(it) }
            ?: postnummerService.ukjent()

    fun toTelefonnummer(telefonnummer: String): String? = telefonnummer.replace(" ", "").takeIf { it != "" }

    private fun toPostnummer(user: User): Postnummer =
        user.addresses
            .map { it.postalCode }
            .map { it[1] }
            .map { postnummerService.findById(it) }
            .firstOrNull { !it.erUkjent() }
            ?: postnummerService.ukjent()

    private fun itOrNull(any: Any?): String? = if (any.toString() != "") any.toString() else null

    private fun String.vask() = replace("&amp;#39;", "'").trim()
}
