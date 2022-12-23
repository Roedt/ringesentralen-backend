package no.roedt.brukere.mfa

import no.roedt.EpostSender
import no.roedt.brukere.Epost
import no.roedt.hypersys.login.AESUtil
import no.roedt.hypersys.login.LoginRequest
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.Dependent

@Dependent
class MFAService(
    private val mfaRepository: MFARepository,
    private val epostSender: EpostSender,
    private val aesUtil: AESUtil,
    @ConfigProperty(name = "aktiverMFA", defaultValue = "true")
    private val mockMFA: Boolean
) {
    fun trengerMFA(mfaRequest: MFARequest) = !mockMFA || !mfaRepository.erVerifisert(dekrypter(mfaRequest))

    private fun dekrypter(mfaRequest: MFARequest): DekryptertMFARequest = DekryptertMFARequest(
        enhetsid = aesUtil.decrypt(mfaRequest.enhetsid),
        brukernavn = aesUtil.decrypt(mfaRequest.brukernavn)
    )

    private fun dekrypter(loginRequest: LoginRequest): SettVerifisertRequest = SettVerifisertRequest(
        enhetsid = aesUtil.decrypt(loginRequest.enhetsid),
        brukarnamn = aesUtil.decrypt(loginRequest.brukarnamn),
        engangskode = aesUtil.decrypt(loginRequest.engangskode!!)
    )

    fun sendMFA(mfaRequest: MFARequest) {
        val mfa = MFA(
            engangskode = MFA.generer(),
            verifisert = false,
            epost = aesUtil.decrypt(mfaRequest.brukernavn),
            enhetsid = aesUtil.decrypt(mfaRequest.enhetsid)
        )
        mfaRepository.lagre(mfa)
        epostSender.sendEpost(epost = lagEpost(mfa), mottaker = mfa.epost)
    }

    private fun lagEpost(mfa: MFA) =
        Epost(
            tekst = "Din kode for Ringesentralen er ${mfa.engangskode}" +
                System.lineSeparator().repeat(3) +
                "Dette er ein automatisk utsendt e-post.",
            tekstAaLoggeHvisDeaktivert = "MFA sendt til bruker",
            loggFoerSendingTekst = "Person har no fått MFA-kode tilsendt",
            tittel = "E-post frå Raudts Ringesentral"
        )

    fun verifiserEngangskode(loginRequest: LoginRequest) {
        val trengerMFA = trengerMFA(
            MFARequest(
                enhetsid = loginRequest.enhetsid,
                brukernavn = loginRequest.brukarnamn
            )
        )
        if (!trengerMFA) {
            return
        }
        if (!loginRequest.engangskode.isNullOrEmpty() && mfaRepository.matcher(dekrypter(loginRequest))) {
            mfaRepository.settVerifisert(dekrypter(loginRequest))
            return
        }
        throw RuntimeException("Ugyldig engangskode")
    }
}
