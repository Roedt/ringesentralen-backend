package no.roedt.ringesentralen.samtale

import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/samtale")
@Tag(name = "Ring")
class RingController(val ringService: RingService) {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed("ringar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/neste")
    @Operation(summary = "Finn neste person å ringe")
    @Retry
    fun hentNestePersonAaRinge(nestePersonAaRingeRequest: NestePersonAaRingeRequest): RingbarPerson? = ringService.hentNestePersonAaRinge(nestePersonAaRingeRequest)

    @RolesAllowed("ringar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/startSamtale")
    @Operation(summary = "Start samtale")
    @Retry
    fun startSamtale(startSamtaleRequest: StartSamtaleRequest): StartSamtaleResponse = ringService.startSamtale(startSamtaleRequest)

    @RolesAllowed("ringar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/registrerResultatFraSamtale")
    @Operation(summary = "Registrer resultat fra samtale")
    @Retry
    fun registrerResultatFraSamtale(resultatFraSatmtaleRequest: ResultatFraSamtaleRequest): ResultatFraSamtaleResponse = ringService.registrerResultatFraSamtale(resultatFraSatmtaleRequest)


    @RolesAllowed("ringar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/noenRingerTilbake")
    @Operation(summary = "Noen ringer tilbake")
    @Retry
    fun noenRingerTilbake(request: RingerTilbakeRequest): RingbarPerson = ringService.noenRingerTilbake(request)

}