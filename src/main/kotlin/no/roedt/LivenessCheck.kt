package no.roedt

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.health.HealthCheck
import org.eclipse.microprofile.health.HealthCheckResponse
import org.eclipse.microprofile.health.Liveness

@Liveness
@ApplicationScoped
class LivenessCheck : HealthCheck {
    override fun call(): HealthCheckResponse = HealthCheckResponse.up(LivenessCheck::class.simpleName)
}
