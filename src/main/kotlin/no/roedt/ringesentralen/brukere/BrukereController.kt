package no.roedt.ringesentralen.brukere

import no.roedt.brukere.AutentisertGetBrukereRequest
import no.roedt.brukere.AutentisertTilgangsendringRequest
import no.roedt.brukere.Brukerendring
import no.roedt.brukere.Brukerinformasjon
import no.roedt.brukere.GenerellRolle
import no.roedt.brukere.TilgangsendringsRequest
import no.roedt.hypersys.HypersysIdProvider
import no.roedt.ringesentralen.RingespesifikkRolle
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.RolesAllowed
import javax.transaction.Transactional
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@Path("/brukere")
@Tag(name = "Brukere")
@SecurityRequirement(name = "jwt")
class BrukereController(
    val brukereService: BrukereService,
    val tilgangsendringService: TilgangsendringService,
    val jwt: JsonWebToken
) : HypersysIdProvider {

    @RolesAllowed(RingespesifikkRolle.godkjenner, GenerellRolle.admin)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/brukere")
    @Operation(summary = "List ut brukere", description = RingespesifikkRolle.godkjennerAdmin)
    fun getBrukere(@Context ctx: SecurityContext): List<Brukerinformasjon> = brukereService.getBrukere(
        AutentisertGetBrukereRequest(ctx.userId(), jwt.groups)
    )

    @RolesAllowed(RingespesifikkRolle.godkjenner, GenerellRolle.admin)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/aktiver")
    @Operation(summary = "Aktiver ringer", description = RingespesifikkRolle.godkjennerAdmin)
    @Bulkhead(3)
    @Retry
    @Transactional
    fun aktiverRinger(@Context ctx: SecurityContext, godkjennRequest: TilgangsendringsRequest): Brukerendring =
        tilgangsendringService.aktiverRinger(
            AutentisertTilgangsendringRequest(ctx.userId(), godkjennRequest, jwt)
        )

    @RolesAllowed(RingespesifikkRolle.godkjenner, GenerellRolle.admin)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/giTilgangTilAaRingeMedlemmer")
    @Operation(summary = "Gi tilgang til å ringe medlemmer", description = RingespesifikkRolle.godkjennerAdmin)
    @Bulkhead(3)
    @Retry
    @Transactional
    fun giTilgangTilAaRingeMedlemmer(
        @Context ctx: SecurityContext,
        godkjennRequest: TilgangsendringsRequest
    ): Brukerendring = tilgangsendringService.giTilgangTilAaRingeMedlemmer(
        AutentisertTilgangsendringRequest(ctx.userId(), godkjennRequest, jwt)
    )

    @RolesAllowed(RingespesifikkRolle.godkjenner, GenerellRolle.admin)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/fjernTilgangTilAaRingeMedlemmer")
    @Operation(summary = "Fjern tilgang til å ringe medlemmer", description = RingespesifikkRolle.godkjennerAdmin)
    @Bulkhead(3)
    @Retry
    @Transactional
    fun fjernTilgangTilAaRingeMedlemmer(
        @Context ctx: SecurityContext,
        godkjennRequest: TilgangsendringsRequest
    ): Brukerendring = tilgangsendringService.fjernTilgangTilAaRingeMedlemmer(
        AutentisertTilgangsendringRequest(ctx.userId(), godkjennRequest, jwt)
    )

    @RolesAllowed(RingespesifikkRolle.godkjenner, GenerellRolle.admin)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/deaktiver")
    @Operation(summary = "Deaktiver ringer", description = RingespesifikkRolle.godkjennerAdmin)
    @Bulkhead(5)
    @Retry
    @Transactional
    fun deaktiverRinger(@Context ctx: SecurityContext, deaktiverRequest: TilgangsendringsRequest): Brukerendring =
        tilgangsendringService.deaktiverRinger(
            AutentisertTilgangsendringRequest(ctx.userId(), deaktiverRequest, jwt)
        )

    @RolesAllowed(GenerellRolle.admin)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/gjoerTilLokalGodkjenner")
    @Operation(summary = "Gjør til lokal godkjenner", description = GenerellRolle.admin)
    @Bulkhead(5)
    @Retry
    @Transactional
    fun gjoerRingerTilLokalGodkjenner(
        @Context ctx: SecurityContext,
        tilLokalGodkjennerRequest: TilgangsendringsRequest
    ): Brukerendring = tilgangsendringService.gjoerRingerTilLokalGodkjenner(
        AutentisertTilgangsendringRequest(
            ctx.userId(),
            tilLokalGodkjennerRequest,
            jwt
        )
    )

    @RolesAllowed(GenerellRolle.admin)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/fjernSomLokalGodkjenner")
    @Operation(summary = "Fjern som lokal godkjenner", description = GenerellRolle.admin)
    @Bulkhead(5)
    @Retry
    @Transactional
    fun fjernRingerSomLokalGodkjenner(
        @Context ctx: SecurityContext,
        fjernSomLokalGodkjennerRequest: TilgangsendringsRequest
    ): Brukerendring = tilgangsendringService.fjernRingerSomLokalGodkjenner(
        AutentisertTilgangsendringRequest(
            ctx.userId(),
            fjernSomLokalGodkjennerRequest,
            jwt
        )
    )
}
