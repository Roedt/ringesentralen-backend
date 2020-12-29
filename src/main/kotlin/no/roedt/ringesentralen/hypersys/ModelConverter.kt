package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.Brukarinformasjon
import no.roedt.ringesentralen.Fylke
import no.roedt.ringesentralen.Postnummer
import no.roedt.ringesentralen.Telefonnummer
import no.roedt.ringesentralen.hypersys.externalModel.Profile
import no.roedt.ringesentralen.hypersys.externalModel.User
import javax.enterprise.context.Dependent

@Dependent
class ModelConverter {
    fun convert(profile: Profile) : Brukarinformasjon = convert(profile.user)

    private fun convert(user: User): Brukarinformasjon {
        val sisteMellomrom = user.name.lastIndexOf(" ")
        val fornamn = user.name.substring(0, sisteMellomrom)
        val etternamn = user.name.substring(sisteMellomrom+1)
        return Brukarinformasjon(
                fornamn = fornamn,
                etternamn = etternamn,
                epost = user.email,
                telefonnummer = toTelefonnummer(user.phone),
                postnummer = toPostnummer(user),
                fylke = Fylke.NordTroendelag // TODO: Gjer noko lurt her
        )
    }

    private fun toTelefonnummer(phone: String): Telefonnummer {
        val splitted = phone.split(" ")
        return Telefonnummer(landkode = splitted[0], nummer = Integer.parseInt(splitted[1]))
    }

    private fun toPostnummer(user: User) = user.addresses.map { it.postalCode }.map{ it[1] }.map { Postnummer(it) }.firstOrNull() ?: Postnummer("0001")
}