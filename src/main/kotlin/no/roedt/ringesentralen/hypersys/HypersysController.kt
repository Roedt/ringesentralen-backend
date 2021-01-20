package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.hypersys.externalModel.Organisasjonsledd
import no.roedt.ringesentralen.hypersys.externalModel.SingleOrgan
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@ApplicationScoped
@Path("/hypersys")
@Tag(name = "Hypersys-integrasjon")
@SecurityRequirement(name = "jwt")
class HypersysController(val service: HypersysService) {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed("ringar")
    @GET
    @Path("/lokallag")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAlleLokallag() : List<Organisasjonsledd> = service.getAlleLokallag()

    @RolesAllowed("ringar")
    @GET
    @Path("/alleOrgan")
    @Operation(summary = "Hent alle organ på lågaste nivå")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAlleOrganPaaLaagasteNivaa(): List<SingleOrgan> = service.getAlleOrganPaaLaagasteNivaa()

}