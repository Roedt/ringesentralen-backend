package no.roedt.brukere.mfa

import no.roedt.EpostSender
import no.roedt.brukere.Epost
import no.roedt.hypersys.login.LoginRequest
import no.roedt.person.Person
import javax.enterprise.context.Dependent

@Dependent
class MFAService(
    private val mfaRepository: MFARepository,
    private val epostSender: EpostSender
) {
    fun trengerMFA(mfaRequest: MFARequest) = !mfaRepository.fins(mfaRequest)

    fun trengerMFA(mfaRequest: MFARequest, epost: String) = !mfaRepository.finsForPerson(mfaRequest, epost)

    fun lagreOgSend(person: Person, enhetsid: String) {
        val mfa = MFA(
            code = MFA.generer(),
            verifisert = false,
            person = person.email!!,
            enhetsid = enhetsid
        )
        mfaRepository.persist(mfa)
        epostSender.sendEpost(person = person, epost = lagEpost(mfa, person))
    }

    private fun lagEpost(mfa: MFA, person: Person) =
        Epost(
            tekst = "Din kode for Ringesentralen er ${mfa.code}" +
                System.lineSeparator().repeat(3) +
                "Dette er ein automatisk utsendt e-post.",
            tekstAaLoggeHvisDeaktivert = "MFA til ${person.id}",
            loggFoerSendingTekst = "Person med id ${person.id} har no fått MFA-kode tilsendt",
            tittel = "E-post frå Raudts Ringesentral"
        )

    fun verifiserEngangskode(loginRequest: LoginRequest) {
        val trengerMFA = trengerMFA(MFARequest(enhetsid = loginRequest.enhetsid), epost = loginRequest.brukarnamn)
        if (!trengerMFA) {
            return
        }
        if (mfaRepository.matcher(enhetsid = loginRequest.enhetsid, epost = loginRequest.brukarnamn, engangskode = loginRequest.engangskode!!)) {
            return
        }
        throw RuntimeException("Ugyldig engangskode")
    }
}
