package no.roedt.token

import no.roedt.brukere.mfa.MFARequest
import no.roedt.hypersys.login.LoginRequest

interface TokenService {

    fun login(loginRequest: LoginRequest): String

    fun trengerMFA(mfaRequest: MFARequest): Boolean

    fun sendMFA(mfaRequest: MFARequest)
}
