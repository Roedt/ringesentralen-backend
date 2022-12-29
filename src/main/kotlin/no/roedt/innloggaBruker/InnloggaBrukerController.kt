package no.roedt.innloggaBruker

import no.roedt.brukere.GenerellRolle
import no.roedt.hypersys.HypersysIdProvider
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@Path("/profil")
@Tag(name = "Profil")
@SecurityRequirement(name = "jwt")
class InnloggaBrukerController(val innloggaBrukerService: InnloggaBrukerService, val jwt: JsonWebToken) :
    HypersysIdProvider {

    @RolesAllowed(GenerellRolle.bruker, GenerellRolle.venterPaaGodkjenning)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    @Operation(summary = "Hent informasjon om innlogga bruker", description = GenerellRolle.brukerVenterPaaGodkjenning)
    @Bulkhead(5)
    @Retry
    fun getProfil(@Context ctx: SecurityContext) = innloggaBrukerService.getProfil(ctx.userId())

    @RolesAllowed(GenerellRolle.bruker)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/lokallag")
    @Operation(summary = "Hent mine tilgjengelege lokallag", description = GenerellRolle.bruker)
    @Bulkhead(3)
    @Retry
    fun getLokallag(@Context ctx: SecurityContext) = innloggaBrukerService.getLokallag(ctx.userId(), jwt.groups)

    @RolesAllowed(GenerellRolle.bruker)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roller")
    @Operation(summary = "Hent mine oppdaterte roller", description = GenerellRolle.bruker)
    @Bulkhead(3)
    @Retry
    fun hentOppdaterteRoller(@Context ctx: SecurityContext) = innloggaBrukerService.getRoller(ctx.userId())
}
