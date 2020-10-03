package no.roedt.ringesentralen

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/ring")
class RingController(val ringService: RingService) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    fun hentNestePersonAaRinge(@QueryParam("lokallagID") nestePersonAaRingeRequest: Int): RingbarPerson? = ringService.hentNestePersonAaRinge(nestePersonAaRingeRequest)

}