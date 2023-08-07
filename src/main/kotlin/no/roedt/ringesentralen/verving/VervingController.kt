package no.roedt.ringesentralen.verving

import jakarta.annotation.security.RolesAllowed
import jakarta.transaction.Transactional
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.SecurityContext
import no.roedt.brukere.GenerellRolle
import no.roedt.hypersys.userId
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import java.net.URI

@Path("/verving")
@Tag(name = "Verving")
@SecurityRequirement(name = "jwt")
class VervingController(val service: VervingService, val jwt: JsonWebToken) {

    @jakarta.annotation.security.RolesAllowed(GenerellRolle.systembrukerFrontend)
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
    @Operation(
        summary = "Registrer svar fra personen på om hen vil ringes",
        description = GenerellRolle.systembrukerFrontend
    )
    @Bulkhead(3)
    @Retry
    fun mottaSvar(@Context ctx: SecurityContext, request: MottaSvarRequest) = service.mottaSvar(
        AutentisertMottaSvarRequest(request = request, userId = ctx.userId())
    )
}
