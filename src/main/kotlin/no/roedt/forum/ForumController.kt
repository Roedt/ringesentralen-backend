package no.roedt.forum

import no.roedt.forum.underforum.Traad
import no.roedt.forum.underforum.TraadRequest
import no.roedt.hypersys.HypersysIdProvider
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.ws.rs.*
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
    @Path("/")
    @Operation(summary = "Finn alle underforum i forumet", description = ForumRolle.debattant)
    @Retry
    fun hentAlleUnderforum(@Context ctx: SecurityContext) = forumService.hentAlleUnderforum()

    @RolesAllowed(ForumRolle.debattant)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/traader/{underforum}")
    @Operation(summary = "Finn alle tråder i et underforum", description = ForumRolle.debattant)
    @Retry
    fun hentTraaderForUnderforum(@Context ctx: SecurityContext, @PathParam("underforum") underforum: String) =
        forumService.hentTraader(underforum)

    @RolesAllowed(ForumRolle.debattant)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/traader/traad/{underforum}/{tittel}")
    @Operation(summary = "Finn en gitt tråd", description = ForumRolle.debattant)
    @Retry
    fun hentTraad(
        @Context ctx: SecurityContext,
        @PathParam("underforum") underforum: String,
        @PathParam("tittel") tittel: String
    ) = forumService.hentTraad(Traad(tittel = tittel, underforum = underforum))

    @RolesAllowed(ForumRolle.debattant)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/traader/traad/{underforum}/{traad}")
    @Operation(summary = "Opprett en tråd", description = ForumRolle.debattant)
    @Retry
    fun opprettTraad(
        @Context ctx: SecurityContext,
        @PathParam("underforum") underforum: String,
        @PathParam("traad") traad: String,
        request: LinkedHashMap<String, Any>
    ) = forumService.opprettTraad(
        TraadRequest(
            underforum = underforum,
            id = traad,
            node = request
        )
    )
}
