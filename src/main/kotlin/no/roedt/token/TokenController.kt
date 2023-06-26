package no.roedt.token

import jakarta.annotation.security.PermitAll
import jakarta.transaction.Transactional
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import no.roedt.brukere.mfa.MFARequest
import no.roedt.hypersys.login.LoginRequest
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag

@Path("/token")
@Tag(name = "Token")
@SecurityRequirement(name = "jwt")
class TokenController(private val tokenService: TokenService, val jwt: JsonWebToken) {

    @PermitAll
    @POST
    @Path("/login")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    fun login(loginRequest: LoginRequest) = tokenService.login(loginRequest)

    @PermitAll
    @POST
    @Path("/trengerMFA")
    @Produces(MediaType.TEXT_PLAIN)
    fun trengerMFA(mfaRequest: MFARequest) = tokenService.trengerMFA(mfaRequest)

    @PermitAll
    @POST
    @Path("/sendMFA")
    @Transactional
    fun sendMFA(mfaRequest: MFARequest) = tokenService.sendMFA(mfaRequest)
}
