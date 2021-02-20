package no.roedt.ringesentralen.historikk

import no.roedt.ringesentralen.RingesentralenController
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.metrics.annotation.Counted
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

    @RolesAllowed("ringer")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/meg")
    @Operation(summary = "Historikk over mine samtaler")
    @Bulkhead(5)
    @Retry
    @Counted
    fun getMineSamtaler(@Context ctx: SecurityContext) = historikkService.getMineSamtaler(ctx.userId())

    @RolesAllowed("admin", "godkjenner")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/laget")
    @Operation(summary = "Historikk over lagets samtaler")
    @Bulkhead(5)
    @Retry
    @Counted
    fun getLagetsSamtaler(@Context ctx: SecurityContext) = historikkService.getLagetsSamtaler(ctx.userId())

    @RolesAllowed("ringer")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/meg/antall")
    @Operation(summary = "Antall samtaler jeg har tatt")
    @Bulkhead(5)
    @Retry
    @Counted
    fun tellMineSamtaler(@Context ctx: SecurityContext) = historikkService.tellMineSamtaler(ctx.userId())
}