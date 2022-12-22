package no.roedt.token

import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.Produces

@ApplicationScoped
class SecretFactoryProducer(
    @ConfigProperty(name = "brukHypersys", defaultValue = "true")
    private val brukHypersys: Boolean,
    @ConfigProperty(name = "secretManagerProjectId", defaultValue = "")
    val secretManagerProjectId: String
) {

    @Produces
    @ApplicationScoped
    fun secretFactory(): SecretFactory = if (brukHypersys) {
        GCPSecretFactory(secretManagerProjectId = secretManagerProjectId).also { it.setup() }
    } else {
        FakeSecretFactory()
    }
}
