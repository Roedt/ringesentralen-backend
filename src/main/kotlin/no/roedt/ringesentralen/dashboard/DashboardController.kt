package no.roedt.ringesentralen.dashboard

import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.RingesentralenController
import no.roedt.ringesentralen.Roles
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.DefaultValue
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@Path("/dashboard")
@Tag(name = "Dashboard")
@RequestScoped
@SecurityRequirement(name = "jwt")
class DashboardController(val dashboardService: DashboardService) : RingesentralenController {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed(Roles.bruker)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/")
    @Operation(summary = "Dashboard", description = Roles.bruker)
    @Bulkhead(5)
    @Retry
    fun getDashboard(@Context ctx: SecurityContext, @QueryParam("modus") @DefaultValue("Velger") modus: Modus): DashboardResponse = dashboardService.getDashboard(ctx.userId(), modus)
}
