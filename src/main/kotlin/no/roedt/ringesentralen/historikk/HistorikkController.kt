package no.roedt.ringesentralen.historikk

import no.roedt.brukere.GenerelleRoller
import no.roedt.hypersys.HypersysIdProvider
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.Roles
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.ws.rs.DefaultValue
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@Path("/historikk")
@Tag(name = "Historikk")
@SecurityRequirement(name = "jwt")
class HistorikkController(private val historikkService: HistorikkService) : HypersysIdProvider {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed(GenerelleRoller.bruker)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/meg")
    @Operation(summary = "Historikk over mine samtaler", description = GenerelleRoller.bruker)
    @Bulkhead(5)
    @Retry
    fun getMineSamtaler(@Context ctx: SecurityContext, @QueryParam("modus") modus: Modus) = historikkService.getMineSamtaler(ctx.userId(), modus)

    @RolesAllowed(Roles.godkjenner, GenerelleRoller.admin)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/laget")
    @Operation(summary = "Historikk over lagets samtaler", description = Roles.godkjennerAdmin)
    @Bulkhead(5)
    @Retry
    fun getLagetsSamtaler(
        @Context ctx: SecurityContext,
        @QueryParam("modus") modus: Modus,
        @QueryParam("lokallag")
        @DefaultValue("-1")
        lokallag: Int
    ) = historikkService.getLagetsSamtaler(ctx.userId(), modus, lokallag)

    @RolesAllowed(Roles.ringer)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/meg/antall")
    @Operation(summary = "Antall samtaler jeg har tatt", description = Roles.ringer)
    @Bulkhead(5)
    @Retry
    fun tellMineSamtaler(@Context ctx: SecurityContext) = historikkService.tellMineSamtaler(ctx.userId())
}
