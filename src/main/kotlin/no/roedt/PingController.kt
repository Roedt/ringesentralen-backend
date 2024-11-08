package no.roedt

import io.quarkus.arc.Unremovable
import jakarta.annotation.security.PermitAll
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.tags.Tag

@Path("/ping")
@ApplicationScoped
@Tag(name = "Ping")
@Unremovable
class PingController {
    @PermitAll
    @GET
    @Operation(summary = "Ping")
    fun ping(): Response = Response.ok().build()
}
