package no.roedt.brukere.mfa

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.person.Person
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
class MFARepository { // TODO: Arv frå panacherepositorybase her. Må berre få laga databasetabellar etc først
    fun fins(mfaRequest: MFARequest) = false
    fun finsForPerson(mfaRequest: MFARequest, person: Person) = false
    fun persist(mfa: MFA) {}
}
