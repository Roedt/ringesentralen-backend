package no.roedt.person

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.Kilde
import no.roedt.postnummer.Postnummer
import java.time.Instant

@RegisterForReflection
data class PersonOppdatering(
    var hypersysID: Int?,
    var fornavn: String,
    var etternavn: String,
    var telefonnummer: String?,
    var email: String?,
    var postnummer: Postnummer,
    var fylke: Int,
    val kilde: Kilde,
    var lokallag: Int,
    var sistOppdatert: Instant?,
    var groupID: Int,
    var oppdateringskilde: Oppdateringskilde
)

@RegisterForReflection
enum class Oppdateringskilde {
    Hypersys,
    Ukjent,
    RegistrertFrivillig
}
