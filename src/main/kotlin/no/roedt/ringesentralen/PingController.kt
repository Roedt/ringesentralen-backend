package no.roedt.ringesentralen

import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.PermitAll
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response


@Path("/ping")
@Tag(name = "Ping")
@SecurityRequirement(name = "jwt")
class PingController {

    @PermitAll
    @GET
    @Operation(summary = "Ping")
    fun ping(): Response = Response.ok().build()
}