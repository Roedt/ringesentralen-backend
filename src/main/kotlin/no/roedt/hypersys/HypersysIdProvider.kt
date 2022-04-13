package no.roedt.hypersys

import no.roedt.person.UserId
import org.eclipse.microprofile.jwt.JsonWebToken
import javax.ws.rs.core.SecurityContext

interface HypersysIdProvider {
    fun SecurityContext.userId(): UserId = UserId((userPrincipal as JsonWebToken).claim<Any>("hypersys.user_id").get().toString().toInt())
}
