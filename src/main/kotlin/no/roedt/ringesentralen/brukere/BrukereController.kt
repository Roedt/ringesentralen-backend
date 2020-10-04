package no.roedt.ringesentralen.brukere

import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.ws.rs.*
import javax.ws.rs.core.MediaType


@Path("/brukere")
@Tag(name = "Brukere")
class BrukereController(val brukereService: BrukereService) {

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/godkjenn")
    @Operation(summary = "Godkjenn ringer")
    @Bulkhead(5)
    @Retry
    fun godkjennRinger(godkjennRequest: TilgangsendringsRequest): Brukerendring = brukereService.godkjennRinger(godkjennRequest)

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/avslaa")
    @Operation(summary = "Avslå ringer")
    @Bulkhead(5)
    @Retry
    fun avslaaRinger(avslaaRequest: TilgangsendringsRequest): Brukerendring = brukereService.avslaaRinger(avslaaRequest)

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/reaktiver")
    @Operation(summary = "Reaktiver ringer")
    @Bulkhead(5)
    @Retry
    fun reaktiverRinger(reaktiverRequest: TilgangsendringsRequest): Brukerendring = brukereService.reaktiverRinger(reaktiverRequest)

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/deaktiver")
    @Operation(summary = "Deaktiver ringer")
    @Bulkhead(5)
    @Retry
    fun deaktiverRinger(deaktiverRequest: TilgangsendringsRequest): Brukerendring = brukereService.deaktiverRinger(deaktiverRequest)

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/gjoerTilLokalGodkjenner")
    @Operation(summary = "Gjør til lokal godkjenner")
    @Bulkhead(5)
    @Retry
    fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring = brukereService.gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest)

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/fjernSomLokalGodkjenner")
    @Operation(summary = "Fjern som lokal godkjenner")
    @Bulkhead(5)
    @Retry
    fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring = brukereService.fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest)

}