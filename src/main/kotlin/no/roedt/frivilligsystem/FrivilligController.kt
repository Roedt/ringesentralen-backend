package no.roedt.frivilligsystem

import no.roedt.frivilligsystem.kontakt.AutentisertRegistrerKontaktRequest
import no.roedt.frivilligsystem.kontakt.RegistrerKontaktRequest
import no.roedt.frivilligsystem.registrer.RegistrerNyFrivilligRequest
import no.roedt.ringesentralen.person.UserId
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.PermitAll
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext


@Path("/")
@Tag(name = "Frivilligsystem")
@SecurityRequirement(name = "jwt")
class FrivilligController(val frivilligService: FrivilligService) {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed("lokallag", "distrikt", "sentralt")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/alle")
    @Operation(summary = "Finn alle frivillige i laget")
    @Retry
    fun hentAlle(@Context ctx: SecurityContext): List<Frivillig> = frivilligService.hentAlle(ctx.userId())

    @PermitAll
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/registrer")
    @Operation(summary = "Registrer ny frivillig")
    @Retry
    @Transactional
    fun registrerNyFrivillig(@Context ctx: SecurityContext, registrerNyFrivilligRequest: RegistrerNyFrivilligRequest): Frivillig = frivilligService.registrerNyFrivillig(registrerNyFrivilligRequest)

    @RolesAllowed("lokallag", "distrikt", "sentralt")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/registrerKontakt")
    @Operation(summary = "Registrer kontakt med frivillig")
    @Retry
    @Transactional
    fun registrerKontakt(@Context ctx: SecurityContext, registrerKontaktRequest: RegistrerKontaktRequest) = frivilligService.registrerKontakt(
        AutentisertRegistrerKontaktRequest(userId = ctx.userId(), request = registrerKontaktRequest)
    )

    fun SecurityContext.userId(): UserId = UserId((userPrincipal as JsonWebToken).claim<Any>("hypersys.user_id").get().toString().toInt())
}