package no.roedt.ringesentralen.person

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.samtale.TelefonnummerValidator

@RegisterForReflection
data class SlettPersonRequest(@JsonProperty("telefonnummer") val telefonnummer: String) {
    fun validate() = TelefonnummerValidator.validate(telefonnummer)
}
