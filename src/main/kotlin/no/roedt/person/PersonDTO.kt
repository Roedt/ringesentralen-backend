package no.roedt.person

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.Kilde
import java.time.Instant

@RegisterForReflection
data class PersonDTO(
    val id: Int,
    val hypersysID: Int?,
    val fornavn: String,
    val etternavn: String,
    val telefonnummer: String?,
    val email: String?,
    val postnummer: String,
    val fylke: Int,
    val lokallag: Int,
    val groupID: Int,
    val kilde: Kilde,
    val sistOppdatert: Instant?
) {
    companion object {
        fun fra(person: Person): PersonDTO =
            PersonDTO(
                id = person.id,
                hypersysID = person.hypersysID,
                fornavn = person.fornavn,
                etternavn = person.etternavn,
                telefonnummer = person.telefonnummer,
                email = person.email,
                postnummer = person.postnummer.Postnummer,
                fylke = person.fylke,
                lokallag = person.lokallag,
                groupID = person.groupID(),
                kilde = person.kilde,
                sistOppdatert = person.sistOppdatert
            )
    }
}
