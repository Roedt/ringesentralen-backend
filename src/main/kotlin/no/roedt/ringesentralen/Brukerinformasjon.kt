package no.roedt.ringesentralen

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Brukerinformasjon(
        val hypersysID: Int,
        val fornamn: String,
        val etternamn: String,
        val epost: String,
        val telefonnummer: Telefonnummer?,
        val postnummer: Int,
        val fylke: Fylke,
        val lokallag: Lokallag?
) {
        fun toTelefonnummer() : String? {
                if (telefonnummer == null) {
                        return null
                }
                return "'${telefonnummer.nummer}'"
        }
}