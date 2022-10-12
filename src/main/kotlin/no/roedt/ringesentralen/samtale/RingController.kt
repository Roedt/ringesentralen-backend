package no.roedt.ringesentralen.samtale

import no.roedt.hypersys.HypersysIdProvider
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.Roles
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
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.Consumes
import javax.ws.rs.DefaultValue
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@Path("/samtale")
@Tag(name = "Ring")
@SecurityRequirement(name = "jwt")
class RingController(val ringService: RingService) : HypersysIdProvider {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed(Roles.ringer)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/neste")
    @Operation(summary = "Finn neste person å ringe", description = Roles.ringer)
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
        ringService.hentNestePersonAaRinge(AutentisertNestePersonAaRingeRequest(ctx.userId(), modus, lokallag, jwt.groups))

    @RolesAllowed(Roles.ringer)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/startSamtale")
    @Operation(summary = "Start samtale", description = Roles.ringer)
    @Retry
    @Transactional
    fun startSamtale(@Context ctx: SecurityContext, startSamtaleRequest: StartSamtaleRequest, @QueryParam("modus") modus: Modus) =
        ringService.startSamtale(AutentisertStartSamtaleRequest(ctx.userId(), startSamtaleRequest, modus))

    @RolesAllowed(Roles.ringer)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/registrerResultatFraSamtale")
    @Operation(summary = "Registrer resultat fra samtale", description = Roles.ringer)
    @Retry
    @Transactional
    fun registrerResultatFraSamtale(@Context ctx: SecurityContext, resultatFraSamtaleRequest: ResultatFraSamtaleRequest, @QueryParam("modus") modus: Modus) =
        ringService.registrerResultatFraSamtale(
            AutentisertResultatFraSamtaleRequest(ctx.userId(), resultatFraSamtaleRequest, modus)
        )

    @RolesAllowed(Roles.ringer)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/noenRingerTilbake")
    @Operation(summary = "Noen ringer tilbake", description = Roles.ringer)
    @Retry
    @Transactional
    fun noenRingerTilbake(@Context ctx: SecurityContext, request: RingerTilbakeRequest, @QueryParam("modus") modus: Modus): NestePersonAaRingeResponse =
        ringService.noenRingerTilbake(AutentisertRingerTilbakeRequest(ctx.userId(), request, modus, jwt.groups))
}
