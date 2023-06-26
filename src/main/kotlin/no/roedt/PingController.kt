package no.roedt

import jakarta.annotation.security.PermitAll
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.tags.Tag

@Path("/ping")
@Tag(name = "Ping")
class PingController {

    @PermitAll
    @GET
    @Operation(summary = "Ping")
    fun ping(): Response = Response.ok().build()
}
