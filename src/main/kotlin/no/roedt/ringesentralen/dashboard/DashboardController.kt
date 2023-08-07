package no.roedt.ringesentralen.dashboard

import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DefaultValue
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.SecurityContext
import no.roedt.brukere.GenerellRolle
import no.roedt.hypersys.userId
import no.roedt.ringesentralen.Modus
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag

@Path("/dashboard")
@Tag(name = "Dashboard")
@RequestScoped
@SecurityRequirement(name = "jwt")
class DashboardController(val dashboardService: DashboardService, val jwt: JsonWebToken) {

    @RolesAllowed(GenerellRolle.bruker)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/")
    @Operation(summary = "Dashboard", description = GenerellRolle.bruker)
    @Bulkhead(5)
    @Retry
    fun getDashboard(
        @Context ctx: SecurityContext,
        @QueryParam("modus")
        @DefaultValue("Velger")
        modus: Modus
    ): DashboardResponse = dashboardService.getDashboard(ctx.userId(), modus)
}
