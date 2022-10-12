package no.roedt.ringesentralen.verving

import no.roedt.brukere.GenerellRolle
import no.roedt.hypersys.HypersysIdProvider
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import java.net.URI
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.SecurityContext

@Path("/verving")
@Tag(name = "Verving")
@SecurityRequirement(name = "jwt")
class VervingController(val service: VervingService) : HypersysIdProvider {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed(GenerellRolle.systembrukerFrontend)
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/verv")
    @Operation(summary = "Legg til person som skal ringes", description = GenerellRolle.systembrukerFrontend)
    @Bulkhead(3)
    @Retry
    fun postPersonSomSkalRinges(@Context ctx: SecurityContext, request: VervingRequest): Response {
        val person = service.postPersonSomSkalRinges(AutentisertVervingRequest(request = request))
        return if (!person.first) {
            Response.noContent().build()
        } else {
            Response.created(URI.create(person.second.id.toString())).build()
        }
    }

    @RolesAllowed(GenerellRolle.systembrukerFrontend)
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/svar")
    @Operation(summary = "Registrer svar fra personen på om hen vil ringes", description = GenerellRolle.systembrukerFrontend)
    @Bulkhead(3)
    @Retry
    fun mottaSvar(@Context ctx: SecurityContext, request: MottaSvarRequest) = service.mottaSvar(
        AutentisertMottaSvarRequest(request = request, userId = ctx.userId())
    )
}
