package no.roedt.ringesentralen.sms

import no.roedt.brukere.GenerellRolle
import no.roedt.hypersys.HypersysIdProvider
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@Path("/sms")
@Tag(name = "SMS")
@SecurityRequirement(name = "jwt")
@ApplicationScoped
class SMSController(val smsService: SMSService) : HypersysIdProvider {

    @Inject
    lateinit var jwt: JsonWebToken

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
