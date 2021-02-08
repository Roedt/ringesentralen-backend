package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.Telefonnummer
import no.roedt.ringesentralen.hypersys.externalModel.Membership
import no.roedt.ringesentralen.hypersys.externalModel.Profile
import no.roedt.ringesentralen.hypersys.externalModel.User
import no.roedt.ringesentralen.person.Person
import javax.enterprise.context.Dependent
import javax.persistence.EntityManager

interface ModelConverter {
    fun convertToSQL(profile: Profile): String
}

@Dependent
class ModelConverterBean(
    private val entityManager: EntityManager
) : ModelConverter {

    private fun convert(user: User): Person {
        val sisteMellomrom = user.name.lastIndexOf(" ")
        val fornamn = user.name.substring(0, sisteMellomrom)
        val etternamn = user.name.substring(sisteMellomrom+1)
        val postnummer = toPostnummer(user)
        return Person(
            hypersysID = user.id,
            fornavn = fornamn,
            etternavn = etternamn,
            email = user.email,
            telefonnummer = user.phone,
            postnummer = postnummer,
            fylke = toFylke(postnummer),
            lokallag = toLokallag(user.memberships) ?: -1,
            groupID = 0, //Ubrukt her uansett, blir automatisk satt i stored proc-en
            sisteSamtale = 0 // Samme her
        )
    }

    override fun convertToSQL(profile: Profile) = convert(profile.user).toSQL()

    private fun Person.toSQL(): String = "CALL sp_registrerNyBruker(" +
            "'${hypersysID}', " +
            "'${fornavn}', " +
            "'${etternavn}', " +
            "${telefonnummer}, " +
            "'${email}', " +
            "${postnummer}, " +
            "${fylke}," +
            "$lokallag" +
            ")"

    fun toTelefonnummer(telefonnummer: String): Telefonnummer? {
        val splitted = telefonnummer.split(" ")
        return when {
            splitted.size >= 2 -> Telefonnummer(landkode = splitted[0], nummer = Integer.parseInt(splitted[1]))
            else -> null
        }
    }

    private fun toPostnummer(user: User) : Int = user.addresses.map { it.postalCode }.map{ it[1] }.map{ it.toInt() }.firstOrNull() ?: 1

    //TODO fix
    private fun toFylke(postnummer: Int): Int =
        entityManager.createNativeQuery(
            "select fylke.id from `postnummer` p " +
                    "inner join kommune kommune on p.KommuneKode = kommune.nummer " +
                    "inner join `fylker` fylke on fylke.id=kommune.fylke_id where postnummer = $postnummer"
        )
            .resultList
            .map { it as Int }
            .firstOrNull()
            ?: -1


    fun toLokallag(memberships: List<Membership>): Int? =
        memberships
            .sortedByDescending { it.startDate }
            .map { it.organisation }
            .firstOrNull()
}