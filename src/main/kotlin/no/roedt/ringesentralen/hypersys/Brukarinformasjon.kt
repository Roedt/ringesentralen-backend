package no.roedt.ringesentralen.hypersys

data class Brukarinformasjon(
        val fornamn: String,
        val etternamn: String,
        val epost: String,
        val telefonnummer: String,
        val postnummer: String,
        val fylke: Int
)
