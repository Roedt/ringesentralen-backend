package no.roedt.frivilligsystem

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class PostnummerDTO(
    @JsonProperty("postnummer") var postnummer: String
)
{
    fun getPostnummer() = Integer.parseInt(postnummer)
}
