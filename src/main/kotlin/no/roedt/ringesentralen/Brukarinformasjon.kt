package no.roedt.ringesentralen

data class Brukarinformasjon(
        val hypersysID: Int,
        val fornamn: String,
        val etternamn: String,
        val epost: String,
        val telefonnummer: Telefonnummer,
        val postnummer: Postnummer,
        val fylke: Fylke
)