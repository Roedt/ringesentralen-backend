package no.roedt.brukere.mfa

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.hypersys.login.LoginRequest
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
    fun erVerifisert(mfaRequest: MFARequest) = false // fins i databasen for denne adressa på denne dingsen og med verifisert true
    fun persist(mfa: MFA) {}
    fun matcher(enhetsid: String, epost: String, engangskode: String): Boolean {
        return true // TODO
    }
    fun settVerifisert(loginRequest: LoginRequest) {
        TODO("Not yet implemented")
    }
}
