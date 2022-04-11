package no.roedt

import org.eclipse.microprofile.health.HealthCheck
import org.eclipse.microprofile.health.HealthCheckResponse
import org.eclipse.microprofile.health.Liveness
import javax.enterprise.context.ApplicationScoped

@Liveness
@ApplicationScoped
class LivenessCheck : HealthCheck {
    override fun call(): HealthCheckResponse = HealthCheckResponse.up(LivenessCheck::class.simpleName)
}
