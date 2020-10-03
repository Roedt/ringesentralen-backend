package no.roedt.ringesentralen

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/ring")
class RingController(val ringService: RingService) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun hentNestePersonAaRinge(): RingbarPerson? = ringService.hentNestePersonAaRinge()

}