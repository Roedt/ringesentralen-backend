package no.roedt.ringesentralen.innloggaBruker

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Profil(
    var hypersysID: Int?,
    var fornavn: String,
    var etternavn: String,
    var telefonnummer: String?,
    var email: String?,
    var postnummer: Int,
    var fylke: Int,
    var lokallag: Int,
    var rolle: Set<String>,
    var fylkeNavn: String,
    var lokallagNavn: String
)
