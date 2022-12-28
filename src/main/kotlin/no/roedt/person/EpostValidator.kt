package no.roedt.person

import java.util.regex.Pattern
import javax.ws.rs.BadRequestException

class EpostValidator {

    companion object {
        @JvmStatic
        private val pattern: Pattern = Pattern.compile("^[A-Za-z](.*)(@)(.+)(\\.)(.+)")

        fun validate(epost: String) {
            if (!pattern.matcher(epost).matches()) {
                throw BadRequestException("Ugyldig format på epost: $epost")
            }
        }
    }
}
