package no.roedt.frivilligsystem.kontakt

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class RegistrerKontaktRequest (
    @JsonProperty("frivillig_id") var frivillig_id: Int,
    @JsonProperty("tilbakemelding") var tilbakemelding: String
)