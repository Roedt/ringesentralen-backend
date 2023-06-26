package no.roedt.innloggaBruker

import jakarta.annotation.security.RolesAllowed
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.SecurityContext
import no.roedt.brukere.GenerellRolle
import no.roedt.hypersys.HypersysIdProvider
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag

@Path("/profil")
@Tag(name = "Profil")
@SecurityRequirement(name = "jwt")
class InnloggaBrukerController(val innloggaBrukerService: InnloggaBrukerService, val jwt: JsonWebToken) :
    HypersysIdProvider {

    @jakarta.annotation.security.RolesAllowed(GenerellRolle.bruker, GenerellRolle.venterPaaGodkjenning)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    @Operation(summary = "Hent informasjon om innlogga bruker", description = GenerellRolle.brukerVenterPaaGodkjenning)
    @Bulkhead(5)
    @Retry
    fun getProfil(@Context ctx: SecurityContext) = innloggaBrukerService.getProfil(ctx.userId())

    @jakarta.annotation.security.RolesAllowed(GenerellRolle.bruker)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/lokallag")
    @Operation(summary = "Hent mine tilgjengelege lokallag", description = GenerellRolle.bruker)
    @Bulkhead(3)
    @Retry
    fun getLokallag(@Context ctx: SecurityContext) = innloggaBrukerService.getLokallag(ctx.userId(), jwt.groups)

    @jakarta.annotation.security.RolesAllowed(GenerellRolle.venterPaaGodkjenning, GenerellRolle.bruker)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roller")
    @Operation(summary = "Hent mine oppdaterte roller", description = GenerellRolle.brukerVenterPaaGodkjenning)
    @Bulkhead(3)
    @Retry
    fun hentOppdaterteRoller(@Context ctx: SecurityContext) = innloggaBrukerService.getRoller(ctx.userId())
}
