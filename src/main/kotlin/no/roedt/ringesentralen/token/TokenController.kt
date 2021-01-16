package no.roedt.ringesentralen.token

import org.eclipse.microprofile.openapi.annotations.Operation
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
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/")
    @Operation(summary = "Fetch JWT token")
    fun getToken(key: String) : String = tokenGenerator.generateToken(key)
}