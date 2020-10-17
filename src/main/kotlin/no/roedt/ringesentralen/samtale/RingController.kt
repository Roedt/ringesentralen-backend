package no.roedt.ringesentralen.samtale

import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/samtale")
@Tag(name = "Ring")
class RingController(val ringService: RingService) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/neste")
    @Operation(summary = "Finn neste person å ringe")
    @Retry
    fun hentNestePersonAaRinge(@QueryParam("lokallagID") nestePersonAaRingeRequest: Int): RingbarPerson? = ringService.hentNestePersonAaRinge(nestePersonAaRingeRequest)

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/startSamtale")
    @Operation(summary = "Start samtale")
    @Retry
    fun startSamtale(startSamtaleRequest: StartSamtaleRequest): StartSamtaleResponse = ringService.startSamtale(startSamtaleRequest)

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/registrerResultatFraSamtale")
    @Operation(summary = "Registrer resultat fra samtale")
    @Retry
    fun registrerResultatFraSamtale(resultatFraSatmtaleRequest: ResultatFraSamtaleRequest): ResultatFraSamtaleResponse = ringService.registrerResultatFraSamtale(resultatFraSatmtaleRequest)

}