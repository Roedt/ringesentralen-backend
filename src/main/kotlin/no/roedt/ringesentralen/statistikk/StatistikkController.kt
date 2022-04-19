package no.roedt.ringesentralen.statistikk

import no.roedt.brukere.GenerelleRoller
import no.roedt.hypersys.HypersysIdProvider
import no.roedt.ringesentralen.Roles
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@Path("/statistikk")
@Tag(name = "Statistikk")
@SecurityRequirement(name = "jwt")
class StatistikkController(val service: StatistikkService) : HypersysIdProvider {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed(Roles.ringer, Roles.godkjenner, GenerelleRoller.admin)
    @GET
    @Path("/statistikk")
    @Operation(summary = "Hent statistikk", description = Roles.ringerGodkjennerAdmin)
    @Produces(MediaType.APPLICATION_JSON)
    fun getStatistikk(@Context ctx: SecurityContext): StatistikkResponse = service.getStatistikk(jwt.groups)

    @RolesAllowed(Roles.ringer, Roles.godkjenner, GenerelleRoller.admin)
    @GET
    @Path("/ringtFlest")
    @Operation(summary = "Hvem har ringt mest?", description = Roles.ringerGodkjennerAdmin)
    @Produces(MediaType.APPLICATION_JSON)
    fun ringtFlestStatistikk(@Context ctx: SecurityContext): RingtFlestStatistikk =
        service.getRingtMest(ctx.userId().userId)

    @RolesAllowed(GenerelleRoller.admin)
    @GET
    @Path("/lodd")
    @Operation(summary = "Hvem har ringt mest? Henter ut liste til loddtrekning", description = GenerelleRoller.admin)
    @Produces(MediaType.APPLICATION_JSON)
    fun lodd(
        @Context ctx: SecurityContext,
        @QueryParam("fra") fra: String,
        @QueryParam("til") til: String
    ): List<LoddStatistikk> = service.lodd(
        toInstant(fra), toInstant(til)
    )

    private fun toInstant(tidspunkt: String): Instant = if (tidspunkt.contains("T")) {
        Instant.parse(tidspunkt)
    } else
        Instant.from(ZonedDateTime.of(LocalDate.parse(tidspunkt), LocalTime.of(0, 0, 0, 0), ZoneId.of("Europe/Oslo")))
}
