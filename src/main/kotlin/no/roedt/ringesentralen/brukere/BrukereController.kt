package no.roedt.ringesentralen.brukere

import no.roedt.ringesentralen.RingesentralenController
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext


@Path("/brukere")
@Tag(name = "Brukere")
@SecurityRequirement(name = "jwt")
class BrukereController(val brukereService: BrukereService) : RingesentralenController {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed("godkjenner", "admin")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/brukere")
    @Operation(summary ="List ut brukere")
    fun getBrukere(@Context ctx: SecurityContext) : List<Brukerinformasjon> = brukereService.getBrukere()

    @RolesAllowed("godkjenner", "admin")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/godkjenn")
    @Operation(summary = "Godkjenn ringer")
    @Bulkhead(5)
    @Retry
    fun godkjennRinger(@Context ctx: SecurityContext, godkjennRequest: TilgangsendringsRequest): Brukerendring = brukereService.godkjennRinger(AutentisertTilgangsendringRequest(ctx.userId(), godkjennRequest))

    @RolesAllowed("godkjenner", "admin")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/avslaa")
    @Operation(summary = "Avslå ringer")
    @Bulkhead(5)
    @Retry
    fun avslaaRinger(@Context ctx: SecurityContext, avslaaRequest: TilgangsendringsRequest): Brukerendring = brukereService.avslaaRinger(AutentisertTilgangsendringRequest(ctx.userId(), avslaaRequest))

    @RolesAllowed("godkjenner", "admin")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/reaktiver")
    @Operation(summary = "Reaktiver ringer")
    @Bulkhead(5)
    @Retry
    fun reaktiverRinger(@Context ctx: SecurityContext, reaktiverRequest: TilgangsendringsRequest): Brukerendring = brukereService.reaktiverRinger(AutentisertTilgangsendringRequest(ctx.userId(), reaktiverRequest))

    @RolesAllowed("godkjenner", "admin")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/deaktiver")
    @Operation(summary = "Deaktiver ringer")
    @Bulkhead(5)
    @Retry
    fun deaktiverRinger(@Context ctx: SecurityContext, deaktiverRequest: TilgangsendringsRequest): Brukerendring = brukereService.deaktiverRinger(AutentisertTilgangsendringRequest(ctx.userId(), deaktiverRequest))

    @RolesAllowed("admin")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/gjoerTilLokalGodkjenner")
    @Operation(summary = "Gjør til lokal godkjenner")
    @Bulkhead(5)
    @Retry
    fun gjoerRingerTilLokalGodkjenner(@Context ctx: SecurityContext, tilLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring = brukereService.gjoerRingerTilLokalGodkjenner(AutentisertTilgangsendringRequest(ctx.userId(), tilLokalGodkjennerRequest))

    @RolesAllowed("admin")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/fjernSomLokalGodkjenner")
    @Operation(summary = "Fjern som lokal godkjenner")
    @Bulkhead(5)
    @Retry
    fun fjernRingerSomLokalGodkjenner(@Context ctx: SecurityContext, fjernSomLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring = brukereService.fjernRingerSomLokalGodkjenner(AutentisertTilgangsendringRequest(ctx.userId(), fjernSomLokalGodkjennerRequest))

}