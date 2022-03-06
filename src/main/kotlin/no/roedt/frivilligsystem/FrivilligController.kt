package no.roedt.frivilligsystem

import io.quarkus.narayana.jta.runtime.TransactionConfiguration
import no.roedt.frivilligsystem.kontakt.AutentisertRegistrerKontaktRequest
import no.roedt.frivilligsystem.kontakt.RegistrerKontaktRequest
import no.roedt.frivilligsystem.registrer.Aktivitet
import no.roedt.frivilligsystem.registrer.AutentisertRegistrerNyFrivilligRequest
import no.roedt.frivilligsystem.registrer.AutentisertSoMeFrivilligRequest
import no.roedt.frivilligsystem.registrer.RegistrerNyFrivilligRequest
import no.roedt.frivilligsystem.registrer.SoMeFrivilligRequest
import no.roedt.ringesentralen.RingesentralenController
import no.roedt.ringesentralen.Roles
import no.roedt.ringesentralen.brukere.EpostSender
import no.roedt.ringesentralen.person.PersonRepository
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import java.net.URI
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
import javax.ws.rs.core.Response
import javax.ws.rs.core.SecurityContext

@Path("/frivillig")
@Tag(name = "Frivilligsystem")
@SecurityRequirement(name = "jwt")
class FrivilligController(
    val frivilligService: FrivilligService,
    val frivilligImporter: FrivilligImporter,
    val epostSender: EpostSender,
    val personRepository: PersonRepository
) :
    RingesentralenController {

    @Inject
    lateinit var jwt: JsonWebToken

    @RolesAllowed(Roles.ringerMedlemmer, Roles.godkjenner, Roles.admin)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/alle")
    @Operation(summary = "Finn alle frivillige i laget", description = Roles.ringerForMedlemmerGodkjennerAdmin)
    @Retry
    fun hentAlle(@Context ctx: SecurityContext) = frivilligService.hentAlle(ctx.userId(), jwt.groups)

    @RolesAllowed(Roles.ringerMedlemmer, Roles.godkjenner, Roles.admin)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/aktivitet")
    @Operation(
        summary = "Finn alle frivillige som kan bidra med en gitt aktivitet",
        description = Roles.ringerForMedlemmerGodkjennerAdmin
    )
    @Retry
    fun hentAlleForAktivitet(@Context ctx: SecurityContext, aktivitet: Aktivitet) =
        frivilligService.hentAlleForAktivitet(ctx.userId(), jwt.groups, aktivitet)

    @RolesAllowed(Roles.systembrukerFrontend)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/registrer")
    @Operation(summary = "Registrer ny frivillig", description = Roles.systembrukerFrontend)
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

    @RolesAllowed(Roles.ringerMedlemmer, Roles.godkjenner, Roles.admin)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/registrerKontakt")
    @Operation(summary = "Registrer kontakt med frivillig", description = Roles.ringerForMedlemmerGodkjennerAdmin)
    @Retry
    @Transactional
    fun registrerKontakt(@Context ctx: SecurityContext, registrerKontaktRequest: RegistrerKontaktRequest) =
        frivilligService.registrerKontakt(
            AutentisertRegistrerKontaktRequest(userId = ctx.userId(), request = registrerKontaktRequest)
        )

    @POST
    @Path("/importer")
    @Transactional
    @TransactionConfiguration(timeout = 3600)
    @RolesAllowed(Roles.admin)
    @Operation(summary = "Importer frivillige frå csv-fil")
    fun import(@Context ctx: SecurityContext) = frivilligImporter.importer(
        ctx.userId(),
        "frivillige.csv",
        "frivillig-import"
    )

    @RolesAllowed(Roles.systembrukerFrontend)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/registrerSoMeFrivillig")
    @Operation(summary = "Registrer ny SoMe-frivillig", description = Roles.systembrukerFrontend)
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
            return if (!frivillig.first) Response.noContent().build()
            else {
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
