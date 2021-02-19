package no.roedt.ringesentralen.samtale

import java.util.regex.Pattern
import javax.ws.rs.BadRequestException

class TelefonnummerValidator {

    companion object {
        private val pattern: Pattern = Pattern.compile("^\\+[0-9]{10,13}\$\n")

        fun validate(telefonnummer: String) {
            if (!pattern.matcher(telefonnummer).matches()) {
                throw BadRequestException("Ugyldig telefonnummer: $telefonnummer")
            }
        }
    }
}