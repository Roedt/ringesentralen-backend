package no.roedt.brukere.mfa

import javax.enterprise.context.Dependent

@Dependent
class MFAService(
    private val mfaRepository: MFARepository
) {
    fun trengerMFA(mfaRequest: MFARequest) = !mfaRepository.fins(mfaRequest)
}
