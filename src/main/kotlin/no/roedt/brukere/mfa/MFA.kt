package no.roedt.brukere.mfa

import io.quarkus.runtime.annotations.RegisterForReflection
import javax.enterprise.context.Dependent

@RegisterForReflection
data class MFA(
    var code: String,
    var verifisert: Boolean,
    var person: String,
    var enhetsid: String
) {
    companion object {
        fun generer(): String {
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..6)
                .map { allowedChars.random() }
                .joinToString("")
        }
    }
}

@Dependent
class MFARepository { // TODO: Arv frå panacherepositorybase her. Må berre få laga databasetabellar etc først
    fun fins(mfaRequest: MFARequest) = false
    fun finsForPerson(mfaRequest: MFARequest, epost: String) = false
    fun persist(mfa: MFA) {}
    fun matcher(enhetsid: String, epost: String, engangskode: String): Boolean {
        return true // TODO
    }
}
