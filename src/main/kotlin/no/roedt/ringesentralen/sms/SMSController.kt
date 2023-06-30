package no.roedt.ringesentralen.sms

import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.SecurityContext
import no.roedt.brukere.GenerellRolle
import no.roedt.hypersys.HypersysIdProvider
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag

@Path("/sms")
@Tag(name = "SMS")
@SecurityRequirement(name = "jwt")
@ApplicationScoped
class SMSController(val smsService: SMSService, val jwt: JsonWebToken) : HypersysIdProvider {

    @RolesAllowed(GenerellRolle.admin)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/lagre")
    @Operation(summary = "Registrer ny SMS som skal sendast ut", description = GenerellRolle.admin)
    @Retry
    @Transactional
    fun registrerSMSForUtsending(@Context ctx: SecurityContext, request: LagreSMSRequest) =
        smsService.registrerSMSForUtsending(AutentisertLagreSMSRequest(userId = ctx.userId(), request = request))

    @RolesAllowed(GenerellRolle.admin)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/oppdater")
    @Operation(summary = "Oppdater utsendingsstatus", description = GenerellRolle.admin)
    @Retry
    @Transactional
    fun oppdaterUtsendingsstatus(@Context ctx: SecurityContext, request: OppdaterSMSRequest) =
        smsService.oppdaterUtsendingsstatus(AutentisertOppdaterSMSRequest(userId = ctx.userId(), request = request))
}
