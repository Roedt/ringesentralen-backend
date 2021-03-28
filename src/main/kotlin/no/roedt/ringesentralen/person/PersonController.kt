package no.roedt.ringesentralen.person

import no.roedt.ringesentralen.RingesentralenController
import no.roedt.ringesentralen.Roles
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@Path("/person")
@Tag(name ="Person")
@SecurityRequirement(name = "jwt")
class PersonController(val service: PersonService) : RingesentralenController {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed(Roles.systembrukerFrontend)
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    @Operation(summary = "Legg til person som skal ringes", description = Roles.systembrukerFrontend)
    @Bulkhead(3)
    @Retry
    fun postPersonSomSkalRinges(@Context ctx: SecurityContext, request: PersonSomSkalRingesRequest) = service.postPersonSomSkalRinges(AutentisertPersonSomSkalRingesRequest(request = request, userId = ctx.userId()))

    @RolesAllowed(Roles.systembrukerFrontend)
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    @Operation(summary = "Legg til person som skal ringes", description = Roles.systembrukerFrontend)
    @Bulkhead(3)
    @Retry
    fun mottaSvar(@Context ctx: SecurityContext, request: MottaSvarRequest) = service.mottaSvar(AutentisertMottaSvarRequest(request = request, userId = ctx.userId()))

}