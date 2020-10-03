package no.roedt.ringesentralen.brukere

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
    fun godkjennRinger(godkjennRequest: TilgangsendringsRequest): Brukerendring = brukereService.godkjennRinger(godkjennRequest)

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/avslaa")
    @Operation(summary = "Avslå ringer")
    fun avslaaRinger(avslaaRequest: TilgangsendringsRequest): Brukerendring = brukereService.avslaaRinger(avslaaRequest)

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/reaktiver")
    @Operation(summary = "Reaktiver ringer")
    fun reaktiverRinger(reaktiverRequest: TilgangsendringsRequest): Brukerendring = brukereService.reaktiverRinger(reaktiverRequest)

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/deaktiver")
    @Operation(summary = "Deaktiver ringer")
    fun deaktiverRinger(deaktiverRequest: TilgangsendringsRequest): Brukerendring = brukereService.deaktiverRinger(deaktiverRequest)

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/gjoerTilLokalGodkjenner")
    @Operation(summary = "Gjør til lokal godkjenner")
    fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring = brukereService.gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest)

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/fjernSomLokalGodkjenner")
    @Operation(summary = "Fjern som lokal godkjenner")
    fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring = brukereService.fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest)

}