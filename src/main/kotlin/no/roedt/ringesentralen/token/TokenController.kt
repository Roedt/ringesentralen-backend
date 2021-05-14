package no.roedt.ringesentralen.token

import no.roedt.ringesentralen.hypersys.login.LoginRequest
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.PermitAll
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/token")
@Tag(name = "Token")
@SecurityRequirement(name = "jwt")
class TokenController(private val tokenService: TokenService, private val fakeTokenService: FakeTokenService) {

    @Inject
    lateinit var jwt: JsonWebToken

    @ConfigProperty(name = "brukHypersys")
    lateinit var brukHypersys: String

    @PermitAll
    @POST
    @Path("/login")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    fun login(loginRequest: LoginRequest): String {
        return if (brukHypersys.toBoolean()) tokenService.login(loginRequest) else fakeTokenService.login(loginRequest)
    }
}
