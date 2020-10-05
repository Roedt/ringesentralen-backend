package no.roedt.ringesentralen.samtale

import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/ring")
@Tag(name = "Ring")
class RingController(val ringService: RingService) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/neste")
    @Operation(summary = "Finn neste person å ringe")
    @Retry
    fun hentNestePersonAaRinge(@QueryParam("lokallagID") nestePersonAaRingeRequest: Int): RingbarPerson? = ringService.hentNestePersonAaRinge(nestePersonAaRingeRequest)

}