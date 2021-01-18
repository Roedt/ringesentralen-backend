package no.roedt.ringesentralen.token

import no.roedt.ringesentralen.hypersys.LoginRequest
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.PermitAll
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/token")
@Tag(name ="Token")
class TokenController(val tokenGenerator: TokenGenerator) {

    @PermitAll
    @POST
    @Path("/login")
    @Produces(MediaType.TEXT_PLAIN)
    fun login(loginRequest: LoginRequest): String = tokenGenerator.login(loginRequest)

}