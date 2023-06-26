package no.roedt.ringesentralen.samtale

import jakarta.annotation.security.RolesAllowed
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DefaultValue
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.SecurityContext
import no.roedt.hypersys.HypersysIdProvider
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.RingespesifikkRolle
import no.roedt.ringesentralen.samtale.resultat.AutentisertResultatFraSamtaleRequest
import no.roedt.ringesentralen.samtale.resultat.ResultatFraSamtaleRequest
import no.roedt.ringesentralen.samtale.start.AutentisertNestePersonAaRingeRequest
import no.roedt.ringesentralen.samtale.start.AutentisertRingerTilbakeRequest
import no.roedt.ringesentralen.samtale.start.AutentisertStartSamtaleRequest
import no.roedt.ringesentralen.samtale.start.NestePersonAaRingeResponse
import no.roedt.ringesentralen.samtale.start.RingerTilbakeRequest
import no.roedt.ringesentralen.samtale.start.StartSamtaleRequest
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag

@Path("/samtale")
@Tag(name = "Ring")
@SecurityRequirement(name = "jwt")
class RingController(val ringService: RingService, val jwt: JsonWebToken) : HypersysIdProvider {

    @jakarta.annotation.security.RolesAllowed(RingespesifikkRolle.ringer)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/neste")
    @Operation(summary = "Finn neste person å ringe", description = RingespesifikkRolle.ringer)
    @Retry
    @Transactional
    fun hentNestePersonAaRinge(
        @Context ctx: SecurityContext,
        @QueryParam("modus")
        @DefaultValue("Velger")
        modus: Modus,
        @QueryParam("lokallag")
        @DefaultValue("-1")
        lokallag: Int
    ): NestePersonAaRingeResponse? =
        ringService.hentNestePersonAaRinge(
            AutentisertNestePersonAaRingeRequest(
                ctx.userId(),
                modus,
                lokallag,
                jwt.groups
            )
        )

    @jakarta.annotation.security.RolesAllowed(RingespesifikkRolle.ringer)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/startSamtale")
    @Operation(summary = "Start samtale", description = RingespesifikkRolle.ringer)
    @Retry
    @Transactional
    fun startSamtale(
        @Context ctx: SecurityContext,
        startSamtaleRequest: StartSamtaleRequest,
        @QueryParam("modus") modus: Modus
    ) =
        ringService.startSamtale(AutentisertStartSamtaleRequest(ctx.userId(), startSamtaleRequest, modus))

    @jakarta.annotation.security.RolesAllowed(RingespesifikkRolle.ringer)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/registrerResultatFraSamtale")
    @Operation(summary = "Registrer resultat fra samtale", description = RingespesifikkRolle.ringer)
    @Retry
    @Transactional
    fun registrerResultatFraSamtale(
        @Context ctx: SecurityContext,
        resultatFraSamtaleRequest: ResultatFraSamtaleRequest,
        @QueryParam("modus") modus: Modus
    ) =
        ringService.registrerResultatFraSamtale(
            AutentisertResultatFraSamtaleRequest(ctx.userId(), resultatFraSamtaleRequest, modus)
        )

    @RolesAllowed(RingespesifikkRolle.ringer)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/noenRingerTilbake")
    @Operation(summary = "Noen ringer tilbake", description = RingespesifikkRolle.ringer)
    @Retry
    @Transactional
    fun noenRingerTilbake(
        @Context ctx: SecurityContext,
        request: RingerTilbakeRequest,
        @QueryParam("modus") modus: Modus
    ): NestePersonAaRingeResponse =
        ringService.noenRingerTilbake(AutentisertRingerTilbakeRequest(ctx.userId(), request, modus, jwt.groups))
}
