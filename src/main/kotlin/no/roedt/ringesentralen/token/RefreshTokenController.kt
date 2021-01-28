package no.roedt.ringesentralen.token

import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.PermitAll
import javax.inject.Inject
import javax.naming.AuthenticationException
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@Path("/refresh")
@Tag(name = "Refresh")
@SecurityRequirement(name = "jwt")
class RefreshTokenController(val tokenGenerator: TokenGenerator) {

    @Inject
    lateinit var jwt: JsonWebToken

    @PermitAll
    @POST
    @Path("/refresh")
    @Produces(MediaType.TEXT_PLAIN)
    fun refresh(@Context ctx: SecurityContext): String {
        if (!ctx.isSecure) {
            throw AuthenticationException("Not logged in")
        }
        return tokenGenerator.refresh(jwt)
    }
}