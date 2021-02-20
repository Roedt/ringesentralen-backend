package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.RingesentralenController
import no.roedt.ringesentralen.hypersys.externalModel.Organisasjonsledd
import no.roedt.ringesentralen.hypersys.externalModel.SingleOrgan
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.metrics.annotation.Counted
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@ApplicationScoped
@Path("/hypersys")
@Tag(name = "Hypersys-integrasjon")
@SecurityRequirement(name = "jwt")
class HypersysController(val service: HypersysService) : RingesentralenController {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed("admin")
    @GET
    @Path("/lokallag")
    @Produces(MediaType.APPLICATION_JSON)
    @Counted
    fun getAlleLokallag(@Context ctx: SecurityContext) : List<Organisasjonsledd> = service.getAlleLokallag()

    @RolesAllowed("admin")
    @GET
    @Path("/alleOrgan")
    @Operation(summary = "Hent alle organ på lågaste nivå")
    @Produces(MediaType.APPLICATION_JSON)
    @Counted
    fun getAlleOrganPaaLaagasteNivaa(@Context ctx: SecurityContext): List<SingleOrgan> = service.getAlleOrganPaaLaagasteNivaa()

    @RolesAllowed("admin")
    @GET
    @Path("/medlemmer")
    @Operation(summary = "Hent alle medlemmer i lokallaget ditt")
    @Produces(MediaType.APPLICATION_JSON)
    @Counted
    fun getMedlemmer(@Context ctx: SecurityContext): List<Any> = service.getMedlemmer(ctx.userId(), GyldigPersonToken(
        access_token = jwt.claim<String>("hypersys.access_token").get(),
        expires_in = jwt.claim<Any>("hypersys.expires_in").get().toString().toInt(),
        token_type = jwt.claim<String>("hypersys.token_type").get(),
        scope = jwt.claim<String>("hypersys.scope").get(),
        refresh_token = jwt.claim<String>("hypersys.refresh_token").get(),
        user_id = jwt.claim<String>("hypersys.user_id").get()
    ))


}