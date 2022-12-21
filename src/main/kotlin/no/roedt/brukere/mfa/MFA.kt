package no.roedt.brukere.mfa

import io.quarkus.runtime.annotations.RegisterForReflection
import javax.enterprise.context.Dependent

@RegisterForReflection
data class MFA(
    var code: String,
    var verifisert: Boolean,
    var person: Int,
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
class MFARepository {
    fun fins(mfaRequest: MFARequest) = false
}