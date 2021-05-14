package no.roedt.ringesentralen.hypersys.login

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
) : PanacheEntity() {
    constructor() : this(
        hypersysID = 0
    )
}

@ApplicationScoped
class LoginAttemptRepository : PanacheRepository<LoginAttempt>
