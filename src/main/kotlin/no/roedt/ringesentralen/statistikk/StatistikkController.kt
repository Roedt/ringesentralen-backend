package no.roedt.ringesentralen.statistikk

import jakarta.annotation.security.RolesAllowed
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.SecurityContext
import no.roedt.brukere.GenerellRolle
import no.roedt.hypersys.HypersysIdProvider
import no.roedt.ringesentralen.RingespesifikkRolle
import no.roedt.tidssone
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime

@Path("/statistikk")
@Tag(name = "Statistikk")
@SecurityRequirement(name = "jwt")
class StatistikkController(val service: StatistikkService, val jwt: JsonWebToken) : HypersysIdProvider {

    @jakarta.annotation.security.RolesAllowed(
        RingespesifikkRolle.ringer,
        RingespesifikkRolle.godkjenner,
        GenerellRolle.admin
    )
    @GET
    @Path("/statistikk")
    @Operation(summary = "Hent statistikk", description = RingespesifikkRolle.ringerGodkjennerAdmin)
    @Produces(MediaType.APPLICATION_JSON)
    fun getStatistikk(@Context ctx: SecurityContext): StatistikkResponse = service.getStatistikk(jwt.groups)

    @jakarta.annotation.security.RolesAllowed(
        RingespesifikkRolle.ringer,
        RingespesifikkRolle.godkjenner,
        GenerellRolle.admin
    )
    @GET
    @Path("/ringtFlest")
    @Operation(summary = "Hvem har ringt mest?", description = RingespesifikkRolle.ringerGodkjennerAdmin)
    @Produces(MediaType.APPLICATION_JSON)
    fun ringtFlestStatistikk(@Context ctx: SecurityContext): RingtFlestStatistikk =
        service.getRingtMest(ctx.userId().userId)

    @RolesAllowed(GenerellRolle.admin)
    @GET
    @Path("/lodd")
    @Operation(summary = "Hvem har ringt mest? Henter ut liste til loddtrekning", description = GenerellRolle.admin)
    @Produces(MediaType.APPLICATION_JSON)
    fun lodd(
        @Context ctx: SecurityContext,
        @QueryParam("fra") fra: String,
        @QueryParam("til") til: String
    ): List<LoddStatistikk> = service.lodd(
        toInstant(fra),
        toInstant(til)
    )

    private fun toInstant(tidspunkt: String): Instant = if (tidspunkt.contains("T")) {
        Instant.parse(tidspunkt)
    } else {
        Instant.from(ZonedDateTime.of(LocalDate.parse(tidspunkt), LocalTime.MIN, tidssone()))
    }
}
