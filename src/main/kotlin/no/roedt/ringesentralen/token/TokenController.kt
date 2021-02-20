package no.roedt.ringesentralen.token

import no.roedt.ringesentralen.RingesentralenController
import no.roedt.ringesentralen.hypersys.LoginRequest
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.metrics.annotation.Counted
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.PermitAll
import javax.inject.Inject
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/token")
@Tag(name ="Token")
@SecurityRequirement(name = "jwt")
class TokenController(val tokenGenerator: TokenGenerator) : RingesentralenController {

    @Inject
    lateinit var jwt: JsonWebToken

    @PermitAll
    @POST
    @Path("/login")
    @Produces(MediaType.TEXT_PLAIN)
    @Counted
    fun login(loginRequest: LoginRequest): String = tokenGenerator.login(loginRequest)

}