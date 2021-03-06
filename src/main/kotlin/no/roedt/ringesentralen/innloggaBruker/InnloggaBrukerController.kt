package no.roedt.ringesentralen.innloggaBruker

import no.roedt.ringesentralen.RingesentralenController
import no.roedt.ringesentralen.Roles
import org.eclipse.microprofile.faulttolerance.Bulkhead
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

@Path("/profil")
@Tag(name = "Profil")
@SecurityRequirement(name = "jwt")
class InnloggaBrukerController(val innloggaBrukerService: InnloggaBrukerService) : RingesentralenController {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed(Roles.bruker, Roles.venterPaaGodkjenning)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    @Operation(summary = "Hent informasjon om innlogga bruker", description = Roles.brukerVenterPaaGodkjenning)
    @Bulkhead(5)
    @Retry
    fun getProfil(@Context ctx: SecurityContext) = innloggaBrukerService.getProfil(ctx.userId())

    @RolesAllowed(Roles.bruker)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/lokallag")
    @Operation(summary = "Hent mine tilgjengelege lokallag", description = Roles.bruker)
    @Bulkhead(3)
    @Retry
    fun getLokallag(@Context ctx: SecurityContext) = innloggaBrukerService.getLokallag(ctx.userId(), jwt.groups)
}
