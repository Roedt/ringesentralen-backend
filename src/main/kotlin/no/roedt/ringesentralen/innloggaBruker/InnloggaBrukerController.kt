package no.roedt.ringesentralen.innloggaBruker

import no.roedt.ringesentralen.RingesentralenController
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
@Tag(name ="Profil")
@SecurityRequirement(name = "jwt")
class InnloggaBrukerController(val innloggaBrukerService: InnloggaBrukerService) : RingesentralenController {

    @Inject
    lateinit var jwt: JsonWebToken


    @RolesAllowed("uautorisert", "ringer", "godkjenner", "admin")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    @Operation(summary = "Hent informasjon om innlogga bruker")
    @Bulkhead(5)
    @Retry
    fun getProfil(@Context ctx: SecurityContext) = innloggaBrukerService.getProfil(ctx.userId())
}