package no.roedt.frivilligsystem

import no.roedt.frivilligsystem.kontakt.AutentisertRegistrerKontaktRequest
import no.roedt.frivilligsystem.kontakt.RegistrerKontaktRequest
import no.roedt.frivilligsystem.registrer.AutentisertRegistrerNyFrivilligRequest
import no.roedt.frivilligsystem.registrer.RegistrerNyFrivilligRequest
import no.roedt.ringesentralen.RingesentralenController
import no.roedt.ringesentralen.Roles
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext


@Path("/")
@Tag(name = "Frivilligsystem")
@SecurityRequirement(name = "jwt")
class FrivilligController(val frivilligService: FrivilligService) : RingesentralenController {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed(Roles.ringerMedlemmer, Roles.godkjenner, Roles.admin)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/alle")
    @Operation(summary = "Finn alle frivillige i laget", description = Roles.ringerForMedlemmerGodkjennerAdmin)
    @Retry
    fun hentAlle(@Context ctx: SecurityContext) = frivilligService.hentAlle(ctx.userId(), jwt.groups)

    @RolesAllowed(Roles.systembrukerFrontend)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/registrer")
    @Operation(summary = "Registrer ny frivillig", description = Roles.systembrukerFrontend)
    @Retry
    @Transactional
    fun registrerNyFrivillig(@Context ctx: SecurityContext, registrerNyFrivilligRequest: RegistrerNyFrivilligRequest): Frivillig = frivilligService.registrerNyFrivillig(AutentisertRegistrerNyFrivilligRequest(userId = ctx.userId(), request = registrerNyFrivilligRequest))

    @RolesAllowed(Roles.ringerMedlemmer, Roles.godkjenner, Roles.admin)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/registrerKontakt")
    @Operation(summary = "Registrer kontakt med frivillig", description = Roles.ringerForMedlemmerGodkjennerAdmin)
    @Retry
    @Transactional
    fun registrerKontakt(@Context ctx: SecurityContext, registrerKontaktRequest: RegistrerKontaktRequest) = frivilligService.registrerKontakt(
        AutentisertRegistrerKontaktRequest(userId = ctx.userId(), request = registrerKontaktRequest)
    )
}