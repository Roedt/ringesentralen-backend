package no.roedt.ringesentralen.twilio

import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.SecurityContext
import no.roedt.brukere.GenerellRolle
import no.roedt.hypersys.HypersysIdProvider
import no.roedt.ringesentralen.sms.AutentisertSendSMSRequest
import no.roedt.ringesentralen.sms.SendSMSRequest
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag

@Path("/smsutsending")
@Tag(name = "SMSUtsending")
@SecurityRequirement(name = "jwt")
@ApplicationScoped
class SMSUtsendingController(val smsSender: SMSSender) : HypersysIdProvider {

    @RolesAllowed(GenerellRolle.admin)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/send")
    @Operation(summary = "Send ut SMS", description = GenerellRolle.admin)
    @Transactional
    fun sendSMS(@Context ctx: SecurityContext, request: SendSMSRequest) =
        smsSender.sendSMS(AutentisertSendSMSRequest(userId = ctx.userId(), request = request))
}
