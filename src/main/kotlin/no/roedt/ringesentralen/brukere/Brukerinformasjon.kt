package no.roedt.ringesentralen.brukere

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.lokallag.Lokallag
import java.time.Instant

@RegisterForReflection
data class Brukerinformasjon(
    val id: Long,
    val hypersysID: Int,
    val fornavn: String,
    val etternavn: String,
    val epost: String,
    val telefonnummer: String?,
    val postnummer: Int,
    val fylke: Fylke,
    val lokallag: Lokallag?,
    var rolle: Set<String>,
    val registreringstidspunkt: Instant
)
