package no.roedt.forum.database

import com.google.auth.oauth2.GoogleCredentials
import io.quarkus.arc.Priority
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Alternative
import javax.enterprise.inject.Produces

@ApplicationScoped
class FirestoreFactory {

    @Alternative
    @Priority(1)
    @Produces
    fun googleCredentials() = GoogleCredentials.getApplicationDefault()!!
}
