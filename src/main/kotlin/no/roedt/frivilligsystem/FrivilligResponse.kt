package no.roedt.frivilligsystem

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.frivilligsystem.kontakt.KontaktResponse
import no.roedt.frivilligsystem.registrer.AktivitetForFrivillig
import no.roedt.lokallag.Lokallag
import no.roedt.person.Person
import no.roedt.brukere.Fylke

@RegisterForReflection
data class FrivilligResponse(
    val frivillig: Frivillig,
    val person: Person,
    val aktiviteter: List<AktivitetForFrivillig>,
    val fylke: Fylke,
    val lokallag: Lokallag,
    val kontakt: List<KontaktResponse>,
    val opptattAv: List<String>,
    val frivilligKorona: FrivilligKorona?
)
