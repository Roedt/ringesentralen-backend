package no.roedt.ringesentralen.samtale

import RingesentralenController
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

@Path("/samtale")
@Tag(name = "Ring")
@SecurityRequirement(name = "jwt")
class RingController(val ringService: RingService) : RingesentralenController {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed("ringar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/neste")
    @Operation(summary = "Finn neste person å ringe")
    @Retry
    fun hentNestePersonAaRinge(@Context ctx: SecurityContext): NestePersonAaRingeResponse? = ringService.hentNestePersonAaRinge(AutentisertNestePersonAaRingeRequest(ctx.userId()))

    @RolesAllowed("ringar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/startSamtale")
    @Operation(summary = "Start samtale")
    @Retry
    fun startSamtale(@Context ctx: SecurityContext, startSamtaleRequest: StartSamtaleRequest) = ringService.startSamtale(AutentisertStartSamtaleRequest(ctx.userId(), startSamtaleRequest))

    @RolesAllowed("ringar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/registrerResultatFraSamtale")
    @Operation(summary = "Registrer resultat fra samtale")
    @Retry
    fun registrerResultatFraSamtale(@Context ctx: SecurityContext, resultatFraSatmtaleRequest: ResultatFraSamtaleRequest) = ringService.registrerResultatFraSamtale(AutentisertResultatFraSamtaleRequest(ctx.userId(), resultatFraSatmtaleRequest))


    @RolesAllowed("ringar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/noenRingerTilbake")
    @Operation(summary = "Noen ringer tilbake")
    @Retry
    fun noenRingerTilbake(@Context ctx: SecurityContext, request: RingerTilbakeRequest): RingbarPerson = ringService.noenRingerTilbake(AutentisertRingerTilbakeRequest(ctx.userId(), request))

}