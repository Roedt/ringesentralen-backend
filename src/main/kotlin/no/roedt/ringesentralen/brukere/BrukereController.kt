package no.roedt.ringesentralen.brukere

import jakarta.annotation.security.RolesAllowed
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.SecurityContext
import no.roedt.brukere.AutentisertGetBrukereRequest
import no.roedt.brukere.AutentisertTilgangsendringRequest
import no.roedt.brukere.Brukerendring
import no.roedt.brukere.Brukerinformasjon
import no.roedt.brukere.GenerellRolle
import no.roedt.brukere.TilgangsendringsRequest
import no.roedt.hypersys.userId
import no.roedt.ringesentralen.RingespesifikkRolle
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag

@Path("/brukere")
@Tag(name = "Brukere")
@SecurityRequirement(name = "jwt")
class BrukereController(
    val brukereService: BrukereService,
    val tilgangsendringService: TilgangsendringService,
    val jwt: JsonWebToken
) {
    @RolesAllowed(RingespesifikkRolle.GODKJENNER, GenerellRolle.ADMIN)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/brukere")
    @Operation(summary = "List ut brukere", description = RingespesifikkRolle.GODKJENNER_ADMIN)
    fun getBrukere(
        @Context ctx: SecurityContext
    ): List<Brukerinformasjon> =
        brukereService.getBrukere(
            AutentisertGetBrukereRequest(ctx.userId(), jwt.groups)
        )

    @RolesAllowed(RingespesifikkRolle.GODKJENNER, GenerellRolle.ADMIN)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/aktiver")
    @Operation(summary = "Aktiver ringer", description = RingespesifikkRolle.GODKJENNER_ADMIN)
    @Bulkhead(3)
    @Retry
    @Transactional
    fun aktiverRinger(
        @Context ctx: SecurityContext,
        godkjennRequest: TilgangsendringsRequest
    ): Brukerendring =
        tilgangsendringService.aktiverRinger(
            AutentisertTilgangsendringRequest(ctx.userId(), godkjennRequest, jwt)
        )

    @jakarta.annotation.security.RolesAllowed(RingespesifikkRolle.GODKJENNER, GenerellRolle.ADMIN)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/giTilgangTilAaRingeMedlemmer")
    @Operation(summary = "Gi tilgang til å ringe medlemmer", description = RingespesifikkRolle.GODKJENNER_ADMIN)
    @Bulkhead(3)
    @Retry
    @Transactional
    fun giTilgangTilAaRingeMedlemmer(
        @Context ctx: SecurityContext,
        godkjennRequest: TilgangsendringsRequest
    ): Brukerendring =
        tilgangsendringService.giTilgangTilAaRingeMedlemmer(
            AutentisertTilgangsendringRequest(ctx.userId(), godkjennRequest, jwt)
        )

    @jakarta.annotation.security.RolesAllowed(RingespesifikkRolle.GODKJENNER, GenerellRolle.ADMIN)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/fjernTilgangTilAaRingeMedlemmer")
    @Operation(summary = "Fjern tilgang til å ringe medlemmer", description = RingespesifikkRolle.GODKJENNER_ADMIN)
    @Bulkhead(3)
    @Retry
    @Transactional
    fun fjernTilgangTilAaRingeMedlemmer(
        @Context ctx: SecurityContext,
        godkjennRequest: TilgangsendringsRequest
    ): Brukerendring =
        tilgangsendringService.fjernTilgangTilAaRingeMedlemmer(
            AutentisertTilgangsendringRequest(ctx.userId(), godkjennRequest, jwt)
        )

    @jakarta.annotation.security.RolesAllowed(RingespesifikkRolle.GODKJENNER, GenerellRolle.ADMIN)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/deaktiver")
    @Operation(summary = "Deaktiver ringer", description = RingespesifikkRolle.GODKJENNER_ADMIN)
    @Bulkhead(5)
    @Retry
    @Transactional
    fun deaktiverRinger(
        @Context ctx: SecurityContext,
        deaktiverRequest: TilgangsendringsRequest
    ): Brukerendring =
        tilgangsendringService.deaktiverRinger(
            AutentisertTilgangsendringRequest(ctx.userId(), deaktiverRequest, jwt)
        )

    @RolesAllowed(GenerellRolle.ADMIN)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/gjoerTilLokalGodkjenner")
    @Operation(summary = "Gjør til lokal godkjenner", description = GenerellRolle.ADMIN)
    @Bulkhead(5)
    @Retry
    @Transactional
    fun gjoerRingerTilLokalGodkjenner(
        @Context ctx: SecurityContext,
        tilLokalGodkjennerRequest: TilgangsendringsRequest
    ): Brukerendring =
        tilgangsendringService.gjoerRingerTilLokalGodkjenner(
            AutentisertTilgangsendringRequest(
                ctx.userId(),
                tilLokalGodkjennerRequest,
                jwt
            )
        )

    @jakarta.annotation.security.RolesAllowed(GenerellRolle.ADMIN)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/fjernSomLokalGodkjenner")
    @Operation(summary = "Fjern som lokal godkjenner", description = GenerellRolle.ADMIN)
    @Bulkhead(5)
    @Retry
    @Transactional
    fun fjernRingerSomLokalGodkjenner(
        @Context ctx: SecurityContext,
        fjernSomLokalGodkjennerRequest: TilgangsendringsRequest
    ): Brukerendring =
        tilgangsendringService.fjernRingerSomLokalGodkjenner(
            AutentisertTilgangsendringRequest(
                ctx.userId(),
                fjernSomLokalGodkjennerRequest,
                jwt
            )
        )
}
