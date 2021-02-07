package no.roedt.ringesentralen.historikk

import RingesentralenController
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

@Path("/historikk")
@Tag(name ="Historikk")
@SecurityRequirement(name = "jwt")
class HistorikkController(private val historikkService: HistorikkService) : RingesentralenController {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed("ringar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    @Operation(summary = "Historikk over mine samtaler")
    @Bulkhead(5)
    @Retry
    fun getMineSamtaler(@Context ctx: SecurityContext) = historikkService.getMineSataler(ctx.userId())
}