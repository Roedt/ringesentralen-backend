package no.roedt.ringesentralen

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Brukarinformasjon(
        val hypersysID: Int,
        val fornamn: String,
        val etternamn: String,
        val epost: String,
        val telefonnummer: Telefonnummer?,
        val postnummer: Postnummer,
        val fylke: Fylke,
        val lokallag: Lokallag?
)