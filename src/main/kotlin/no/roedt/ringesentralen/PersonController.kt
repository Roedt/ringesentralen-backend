package no.roedt.ringesentralen

import no.roedt.ringesentralen.person.Person
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.RolesAllowed
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext


@Path("/person")
@Tag(name = "Person")
@SecurityRequirement(name = "jwt")
class PersonController(
    private val databaseUpdater: DatabaseUpdater,
    private val personRepository: PersonRepository
) : RingesentralenController {

    @RolesAllowed("admin")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/slett")
    @Operation(summary = "Slett person fra systemet")
    @Bulkhead(5)
    @Retry
    fun slettPerson(@Context ctx: SecurityContext, slettPersonRequest : SlettPersonRequest) {
        val id = personRepository.find("telefonnummer", slettPersonRequest.telefonnummer).firstResult<Person>().id
        databaseUpdater.update("CALL sp_slettPerson($id)")
    }
}