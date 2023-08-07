package no.roedt.hypersys

import jakarta.ws.rs.core.SecurityContext
import no.roedt.person.UserId
import org.eclipse.microprofile.jwt.JsonWebToken

fun SecurityContext.userId(): UserId =
    UserId((userPrincipal as JsonWebToken).claim<Any>("hypersys.user_id").get().toString().toInt())
