package no.roedt.forum.database

import com.google.auth.oauth2.GoogleCredentials
import io.quarkus.arc.Priority
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Alternative
import jakarta.enterprise.inject.Produces

@ApplicationScoped
class FirestoreFactory {

    @Alternative
    @Priority(1)
    @Produces
    fun googleCredentials() = GoogleCredentials.getApplicationDefault()!!
}
