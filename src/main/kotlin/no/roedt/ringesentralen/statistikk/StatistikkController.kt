package no.roedt.ringesentralen.statistikk

import no.roedt.ringesentralen.RingesentralenController
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.metrics.annotation.Counted
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

@Path("/statistikk")
@Tag(name = "Statistikk")
@SecurityRequirement(name = "jwt")
class StatistikkController(val service: StatistikkService) : RingesentralenController {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed("admin")
    @GET
    @Path("/statistikk")
    @Produces(MediaType.APPLICATION_JSON)
    @Counted
    fun getStatistikk(@Context ctx: SecurityContext) : StatistikkResponse = service.getStatistikk()
}