package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.hypersys.externalModel.Organisasjonsledd
import no.roedt.ringesentralen.hypersys.externalModel.SingleOrgan
import org.eclipse.microprofile.openapi.annotations.Operation
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
class HypersysController(val service: HypersysService) {

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    fun login(loginRequest: LoginRequest): Token = service.login(loginRequest)

    @GET
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    fun getTokenFromHypersys(): Token = service.getTokenFromHypersys()

    @GET
    @Path("/lokallag")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAlleLokallag() : List<Organisasjonsledd> = service.getAlleLokallag()

    @GET
    @Path("/alleOrgan")
    @Operation(summary = "Hent alle organ på lågaste nivå")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAlleOrganPaaLaagasteNivaa(): List<SingleOrgan> = service.getAlleOrganPaaLaagasteNivaa()

}