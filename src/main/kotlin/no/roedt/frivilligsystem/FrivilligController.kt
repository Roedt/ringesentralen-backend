package no.roedt.frivilligsystem

import no.roedt.brukere.GenerellRolle
import no.roedt.frivilligsystem.kontakt.AutentisertRegistrerKontaktRequest
import no.roedt.frivilligsystem.kontakt.RegistrerKontaktRequest
import no.roedt.frivilligsystem.registrer.Aktivitet
import no.roedt.frivilligsystem.registrer.AutentisertRegistrerNyFrivilligRequest
import no.roedt.frivilligsystem.registrer.AutentisertSoMeFrivilligRequest
import no.roedt.frivilligsystem.registrer.RegistrerNyFrivilligRequest
import no.roedt.frivilligsystem.registrer.SoMeFrivilligRequest
import no.roedt.hypersys.HypersysIdProvider
import no.roedt.person.PersonRepository
import no.roedt.ringesentralen.RingespesifikkRolle
import no.roedt.ringesentralen.brukere.RingesentralenEpostformulerer
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import java.net.URI
import javax.annotation.security.RolesAllowed
import javax.transaction.Transactional
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.SecurityContext

@Path("/frivillig")
@Tag(name = "Frivilligsystem")
@SecurityRequirement(name = "jwt")
class FrivilligController(
    val frivilligService: FrivilligService,
    val epostSender: RingesentralenEpostformulerer,
    val personRepository: PersonRepository,
    val jwt: JsonWebToken
) :
    HypersysIdProvider {

    @RolesAllowed(RingespesifikkRolle.ringerMedlemmer, RingespesifikkRolle.godkjenner, GenerellRolle.admin)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/alle")
    @Operation(summary = "Finn alle frivillige i laget", description = RingespesifikkRolle.ringerForMedlemmerGodkjennerAdmin)
    @Retry
    fun hentAlle(@Context ctx: SecurityContext) = frivilligService.hentAlle(ctx.userId(), jwt.groups)

    @RolesAllowed(RingespesifikkRolle.ringerMedlemmer, RingespesifikkRolle.godkjenner, GenerellRolle.admin)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/aktivitet")
    @Operation(
        summary = "Finn alle frivillige som kan bidra med en gitt aktivitet",
        description = RingespesifikkRolle.ringerForMedlemmerGodkjennerAdmin
    )
    @Retry
    fun hentAlleForAktivitet(@Context ctx: SecurityContext, aktivitet: Aktivitet) =
        frivilligService.hentAlleForAktivitet(ctx.userId(), jwt.groups, aktivitet)

    @RolesAllowed(GenerellRolle.systembrukerFrontend)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/registrer")
    @Operation(summary = "Registrer ny frivillig", description = GenerellRolle.systembrukerFrontend)
    @Retry
    @Transactional
    fun registrerNyFrivillig(
        @Context ctx: SecurityContext,
        registrerNyFrivilligRequest: RegistrerNyFrivilligRequest
    ): Frivillig =
        try {
            frivilligService.registrerNyFrivillig(
                AutentisertRegistrerNyFrivilligRequest(
                    userId = ctx.userId(),
                    request = registrerNyFrivilligRequest
                )
            ).second
        } catch (e: java.lang.NullPointerException) {
            e.printStackTrace()
            System.err.println(registrerNyFrivilligRequest)
            throw e
        }

    @RolesAllowed(RingespesifikkRolle.ringerMedlemmer, RingespesifikkRolle.godkjenner, GenerellRolle.admin)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/registrerKontakt")
    @Operation(summary = "Registrer kontakt med frivillig", description = RingespesifikkRolle.ringerForMedlemmerGodkjennerAdmin)
    @Retry
    @Transactional
    fun registrerKontakt(@Context ctx: SecurityContext, registrerKontaktRequest: RegistrerKontaktRequest) =
        frivilligService.registrerKontakt(
            AutentisertRegistrerKontaktRequest(userId = ctx.userId(), request = registrerKontaktRequest)
        )

    @RolesAllowed(GenerellRolle.systembrukerFrontend)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/registrerSoMeFrivillig")
    @Operation(summary = "Registrer ny SoMe-frivillig", description = GenerellRolle.systembrukerFrontend)
    @Retry
    @Transactional
    fun soMeFrivilligregistrering(
        @Context ctx: SecurityContext,
        request: SoMeFrivilligRequest
    ): Response {
        try {
            val frivillig = frivilligService.registrerNySoMeFrivillig(
                AutentisertSoMeFrivilligRequest(
                    userId = ctx.userId(),
                    request = request
                )
            )
            return if (!frivillig.first) {
                Response.noContent().build()
            } else {
                try {
                    epostSender.sendEpostOmRegistrertSoMeFrivillig(personRepository.findById(frivillig.second.personId))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Response.created(URI.create(frivillig.second.id.toString())).entity(frivillig.second).build()
            }
        } catch (e: java.lang.RuntimeException) {
            e.printStackTrace()
            System.err.println(request)
            throw e
        }
    }
}
