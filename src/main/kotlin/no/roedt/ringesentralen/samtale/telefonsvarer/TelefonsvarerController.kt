package no.roedt.ringesentralen.samtale.telefonsvarer

import jakarta.annotation.security.RolesAllowed
import jakarta.transaction.Transactional
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.SecurityContext
import no.roedt.brukere.GenerellRolle
import no.roedt.hypersys.HypersysIdProvider
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag

@Path("/telefonsvar")
@Tag(name = "Telefonsvar")
@SecurityRequirement(name = "jwt")
class TelefonsvarerController(val telefonsvarerService: TelefonsvarerService, val jwt: JsonWebToken) :
    HypersysIdProvider {

    @RolesAllowed(GenerellRolle.systembrukerFrontend)
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    @Operation(summary = "Registrere resultat frå telefonsvar", description = GenerellRolle.systembrukerFrontend)
    @Bulkhead(5)
    @Retry
    fun postSvarFraTelefonsvarer(@Context ctx: SecurityContext, request: TelefonsvarerRequest) =
        telefonsvarerService.postSvarFraTelefonsvarer(
            AutentisertTelefonsvarerRequest(
                userId = ctx.userId(),
                request = request
            )
        )
}
