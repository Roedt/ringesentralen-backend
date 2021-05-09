package no.roedt.frivilligsystem.registrer

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.frivilligsystem.OpptattAv
import no.roedt.ringesentralen.person.UserId
import java.time.Instant

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
data class RegistrerNyFrivilligRequest(
    @JsonProperty("tidspunkt") var tidspunkt: Instant,
    @JsonProperty("fornavn") var fornavn: String,
    @JsonProperty("etternavn") var etternavn: String,
    @JsonProperty("epost") var epost: String,
    @JsonProperty("telefonnummer") var telefonnummer: String,
    @JsonProperty("postnummer") var postnummer: Int,
    @JsonProperty("alleredeAktivILokallag") var alleredeAktivILokallag: Boolean,
    @JsonProperty("medlemIRoedt") var medlemIRoedt: ErMedlemStatus,
    @JsonProperty("kanTenkeSegAaBidraMedAktiviteter") var kanTenkeSegAaBidraMedAktiviteter: List<Aktivitet>,
    @JsonProperty("spesiellKompetanse") var spesiellKompetanse: String?,
    @JsonProperty("andreTingDuVilBidraMed") var andreTingDuVilBidraMed: String?,
    @JsonProperty("fortellLittOmDegSelv") var fortellLittOmDegSelv: String?,
    @JsonProperty("opptattAv") var opptattAv: List<OpptattAv>,
    @JsonProperty("haandtering") var haandtering: String,
    @JsonProperty("personlig") var personlig: Boolean,
    @JsonProperty("tydelig") var tydelig: String,
    @JsonProperty("forslag") var forslag: String
)

@RegisterForReflection
data class AutentisertRegistrerNyFrivilligRequest(
    val userId: UserId,
    val request: RegistrerNyFrivilligRequest
)