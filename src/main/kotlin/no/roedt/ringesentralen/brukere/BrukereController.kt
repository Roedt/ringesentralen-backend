package no.roedt.ringesentralen.brukere

import org.eclipse.microprofile.openapi.annotations.Operation
import javax.ws.rs.*
import javax.ws.rs.core.MediaType


@Path("/brukere")
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

}