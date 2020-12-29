package no.roedt.ringesentralen

data class Brukarinformasjon(
        val fornamn: String,
        val etternamn: String,
        val epost: String,
        val telefonnummer: Telefonnummer,
        val postnummer: Postnummer,
        val fylke: Fylke
)