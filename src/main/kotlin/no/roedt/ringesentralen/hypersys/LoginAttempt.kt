package no.roedt.ringesentralen.hypersys

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.Table

@RegisterForReflection
@Entity
@Table(name = "login_attempts")
data class LoginAttempt(
    var hypersysID: Int
) : PanacheEntity()

@ApplicationScoped
class LoginAttemptRepository : PanacheRepository<LoginAttempt>