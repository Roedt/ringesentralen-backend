package no.roedt

import com.fasterxml.jackson.core.JsonProcessingException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class ExceptionHandler : ExceptionMapper<JsonProcessingException> {
    override fun toResponse(exception: JsonProcessingException): Response {
        exception.printStackTrace()
        return Response.status(Response.Status.BAD_REQUEST).entity(exception.message).build()
    }
}
