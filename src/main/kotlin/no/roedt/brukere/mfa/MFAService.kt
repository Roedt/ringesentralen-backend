package no.roedt.brukere.mfa

import no.roedt.EpostSender
import no.roedt.brukere.Epost
import no.roedt.hypersys.login.LoginRequest
import javax.enterprise.context.Dependent

@Dependent
class MFAService(
    private val mfaRepository: MFARepository,
    private val epostSender: EpostSender
) {
    fun trengerMFA(mfaRequest: MFARequest) = !mfaRepository.erVerifisert(mfaRequest)

    fun lagreOgSend(mfaRequest: MFARequest) {
        val mfa = MFA(
            code = MFA.generer(),
            verifisert = false,
            person = mfaRequest.brukernavn,
            enhetsid = mfaRequest.enhetsid
        )
        mfaRepository.persist(mfa)
        epostSender.sendEpost(epost = lagEpost(mfa), mottaker = mfaRequest.brukernavn)
    }

    private fun lagEpost(mfa: MFA) =
        Epost(
            tekst = "Din kode for Ringesentralen er ${mfa.code}" +
                System.lineSeparator().repeat(3) +
                "Dette er ein automatisk utsendt e-post.",
            tekstAaLoggeHvisDeaktivert = "MFA sendt til bruker",
            loggFoerSendingTekst = "Person har no fått MFA-kode tilsendt",
            tittel = "E-post frå Raudts Ringesentral"
        )

    fun verifiserEngangskode(loginRequest: LoginRequest) {
        val trengerMFA = trengerMFA(MFARequest(enhetsid = loginRequest.enhetsid, brukernavn = loginRequest.brukarnamn))
        if (!trengerMFA) {
            return
        }
        if (mfaRepository.matcher(enhetsid = loginRequest.enhetsid, epost = loginRequest.brukarnamn, engangskode = loginRequest.engangskode!!)) {
            mfaRepository.settVerifisert(loginRequest)
            return
        }
        throw RuntimeException("Ugyldig engangskode")
    }

    fun sendMFA(mfaRequest: MFARequest) {
        lagreOgSend(mfaRequest)
    }
}
