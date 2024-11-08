package no.roedt.ringesentralen.historikk

import jakarta.enterprise.context.RequestScoped
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
import no.roedt.ringesentralen.RingespesifikkRolle
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag

@Path("/historikk")
@Tag(name = "Historikk")
@SecurityRequirement(name = "jwt")
@RequestScoped
class HistorikkController(private val historikkService: HistorikkService, val jwt: JsonWebToken) {
    @jakarta.annotation.security.RolesAllowed(GenerellRolle.BRUKER)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/meg")
    @Operation(summary = "Historikk over mine samtaler", description = GenerellRolle.BRUKER)
    @Bulkhead(5)
    @Retry
    fun getMineSamtaler(
        @Context ctx: SecurityContext,
        @QueryParam("modus") modus: Modus
    ) = historikkService.getMineSamtaler(ctx.userId(), modus)

    @jakarta.annotation.security.RolesAllowed(RingespesifikkRolle.GODKJENNER, GenerellRolle.ADMIN)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/laget")
    @Operation(summary = "Historikk over lagets samtaler", description = RingespesifikkRolle.GODKJENNER_ADMIN)
    @Bulkhead(5)
    @Retry
    fun getLagetsSamtaler(
        @Context ctx: SecurityContext,
        @QueryParam("modus") modus: Modus,
        @QueryParam("lokallag")
        @DefaultValue("-1")
        lokallag: Int
    ) = historikkService.getLagetsSamtaler(ctx.userId(), modus, lokallag)

    @jakarta.annotation.security.RolesAllowed(RingespesifikkRolle.RINGER)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/meg/antall")
    @Operation(summary = "Antall samtaler jeg har tatt", description = RingespesifikkRolle.RINGER)
    @Bulkhead(5)
    @Retry
    fun tellMineSamtaler(
        @Context ctx: SecurityContext
    ) = historikkService.tellMineSamtaler(ctx.userId())
}
