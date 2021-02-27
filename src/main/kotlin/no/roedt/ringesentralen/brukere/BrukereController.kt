package no.roedt.ringesentralen.brukere

import no.roedt.ringesentralen.RingesentralenController
import no.roedt.ringesentralen.Roles
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

    @RolesAllowed(Roles.godkjenner, Roles.admin)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/brukere")
    @Operation(summary ="List ut brukere", description = Roles.godkjennerAdmin)
    fun getBrukere(@Context ctx: SecurityContext) : List<Brukerinformasjon> = brukereService.getBrukere()

    @RolesAllowed(Roles.godkjenner, Roles.admin)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/aktiver")
    @Operation(summary = "Aktiver ringer", description = Roles.godkjennerAdmin)
    @Bulkhead(5)
    @Retry
    fun aktiverRinger(@Context ctx: SecurityContext, godkjennRequest: TilgangsendringsRequest): Brukerendring = brukereService.aktiverRinger(AutentisertTilgangsendringRequest(ctx.userId(), godkjennRequest))

    @RolesAllowed(Roles.godkjenner, Roles.admin)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/deaktiver")
    @Operation(summary = "Deaktiver ringer", description = Roles.godkjennerAdmin)
    @Bulkhead(5)
    @Retry
    fun deaktiverRinger(@Context ctx: SecurityContext, deaktiverRequest: TilgangsendringsRequest): Brukerendring = brukereService.deaktiverRinger(AutentisertTilgangsendringRequest(ctx.userId(), deaktiverRequest))

    @RolesAllowed(Roles.admin)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/gjoerTilLokalGodkjenner")
    @Operation(summary = "Gjør til lokal godkjenner", description = Roles.admin)
    @Bulkhead(5)
    @Retry
    fun gjoerRingerTilLokalGodkjenner(@Context ctx: SecurityContext, tilLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring = brukereService.gjoerRingerTilLokalGodkjenner(AutentisertTilgangsendringRequest(ctx.userId(), tilLokalGodkjennerRequest))

    @RolesAllowed(Roles.admin)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/fjernSomLokalGodkjenner")
    @Operation(summary = "Fjern som lokal godkjenner", description = Roles.admin)
    @Bulkhead(5)
    @Retry
    fun fjernRingerSomLokalGodkjenner(@Context ctx: SecurityContext, fjernSomLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring = brukereService.fjernRingerSomLokalGodkjenner(AutentisertTilgangsendringRequest(ctx.userId(), fjernSomLokalGodkjennerRequest))

}