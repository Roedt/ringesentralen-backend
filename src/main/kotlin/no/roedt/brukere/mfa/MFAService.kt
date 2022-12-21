package no.roedt.brukere.mfa

import no.roedt.EpostSender
import no.roedt.brukere.Epost
import no.roedt.hypersys.login.AESUtil
import no.roedt.hypersys.login.LoginRequest
import javax.enterprise.context.Dependent

@Dependent
class MFAService(
    private val mfaRepository: MFARepository,
    private val epostSender: EpostSender,
    private val aesUtil: AESUtil
) {
    fun trengerMFA(mfaRequest: MFARequest) = !mfaRepository.erVerifisert(mfaRequest)

    fun lagreOgSend(mfaRequest: MFARequest) {
        val mfa = MFA(
            code = MFA.generer(),
            verifisert = false,
            person = aesUtil.decrypt(mfaRequest.brukernavn),
            enhetsid = aesUtil.decrypt(mfaRequest.enhetsid)
        )
        mfaRepository.persist(mfa)
        epostSender.sendEpost(epost = lagEpost(mfa), mottaker = mfa.person)
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
        val trengerMFA = trengerMFA(
            MFARequest(
                enhetsid = aesUtil.decrypt(loginRequest.enhetsid),
                brukernavn = aesUtil.decrypt(loginRequest.brukarnamn)
            )
        )
        if (!trengerMFA) {
            return
        }
        if (mfaRepository.matcher(
                enhetsid = aesUtil.decrypt(loginRequest.enhetsid),
                epost = aesUtil.decrypt(loginRequest.brukarnamn),
                engangskode = aesUtil.decrypt(loginRequest.engangskode!!)
            )
        ) {
            mfaRepository.settVerifisert(loginRequest)
            return
        }
        throw RuntimeException("Ugyldig engangskode")
    }

    fun sendMFA(mfaRequest: MFARequest) {
        lagreOgSend(mfaRequest)
    }
}
