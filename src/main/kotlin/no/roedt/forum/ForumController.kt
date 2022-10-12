package no.roedt.forum

import no.roedt.hypersys.HypersysIdProvider
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@Path("/forum")
@Tag(name = "Forum")
@SecurityRequirement(name = "jwt")
class ForumController(
    val forumService: ForumService
) : HypersysIdProvider {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed(ForumRolle.debattant)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/underforum")
    @Operation(summary = "Finn alle underforum i forumet", description = ForumRolle.debattant)
    @Retry
    fun hentAlleUnderforum(@Context ctx: SecurityContext) = forumService.hentAlleUnderforum()
}