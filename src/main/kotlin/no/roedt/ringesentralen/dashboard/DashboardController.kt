package no.roedt.ringesentralen.dashboard

import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/dashboard")
@Tag(name = "Dashboard")
@RequestScoped
class DashboardController(val dashboardService: DashboardService) {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed("ringar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/")
    @Operation(summary = "Dashboard")
    @Bulkhead(5)
    @Retry
    fun getDashboard(@QueryParam("ringerID") ringerID: Long): DashboardResponse = dashboardService.getDashboard(ringerID)
}