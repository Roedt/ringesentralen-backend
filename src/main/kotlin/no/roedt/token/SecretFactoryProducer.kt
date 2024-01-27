package no.roedt.token

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Produces
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class SecretFactoryProducer(
    @ConfigProperty(name = "brukHypersys", defaultValue = "true")
    private val brukHypersys: Boolean,
    @ConfigProperty(name = "secretManagerProjectId", defaultValue = "")
    val secretManagerProjectId: String
) {
    @Produces
    @ApplicationScoped
    fun secretFactory(): SecretFactory =
        if (brukHypersys) {
            GCPSecretFactory(secretManagerProjectId = secretManagerProjectId).also { it.setup() }
        } else {
            FakeSecretFactory()
        }
}
