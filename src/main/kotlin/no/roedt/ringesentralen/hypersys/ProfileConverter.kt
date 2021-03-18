package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.hypersys.externalModel.Address
import no.roedt.ringesentralen.hypersys.externalModel.Profile
import no.roedt.ringesentralen.hypersys.externalModel.User
import no.roedt.ringesentralen.person.Person
import javax.enterprise.context.Dependent

@Dependent
class ProfileConverter {

    fun convertToProfile(map: Map<*, *>) : Profile = map.toProfile()

    private fun Map<*,*>.toProfile() : Profile =
        Profile(
            user = User(
                id = get("member_id").toString().toInt(),
                name = get("name").toString(),
                email = get("email").toString(),
                phone = get("mobile").toString(),
                phone2 = get("secondary_phone").toString(),
                roles = listOf(),
                memberships = listOf(),
                addresses = listOf(toAddress(get("postal_address")))
            )
        )

    private fun toAddress(get: Any?): Address {
        val address = get as Map<String, String>
        return Address(
            id = 1,
            name = address.get("address1").toString(),
            address1 = address.get("address1").toString(),
            address2 = address.get("address2").toString(),
            subject = "",
            postalCode = listOf(address.get("postal_code").toString())
        )
    }

    fun convertToPerson(it: Profile) : Person {
        return Person(
            hypersysID = it.user.id,
            fornavn = it.user.name,
            etternavn = it.user.name,
            telefonnummer = if (it.user.phone != "") it.user.phone else null,
            email = if (it.user.email != "") it.user.email else null,
            postnummer = it.user.addresses.flatMap { it.postalCode }.firstOrNull { i -> i != "null" }?.toString()?.toInt() ?: -1,
            fylke = -1,
            groupID = 1,
            lokallag = -1
        )
    }
}