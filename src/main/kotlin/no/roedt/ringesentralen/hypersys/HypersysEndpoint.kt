package no.roedt.ringesentralen.hypersys

import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@ApplicationScoped
@Path("/hypersys")
@Tag(name = "Hypersys-integrasjon")
class HypersysEndpoint(val service: HypersysService) {

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    fun login(loginRequest: LoginRequest): GyldigToken = service.login(loginRequest)

    @GET
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    fun getTokenFromHypersys(): GyldigToken = service.getTokenFromHypersys()

    @GET
    @Path("/lokallag")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAlleLokallag() : List<Organisasjonsledd> = service.getAlleLokallag()

    @GET
    @Path("/alleOrgan")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAlleOrganPaaLaagasteNivaa(): List<SingleOrgan> = service.getAlleOrganPaaLaagasteNivaa()

}