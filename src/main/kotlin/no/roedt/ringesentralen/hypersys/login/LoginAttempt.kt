package no.roedt.ringesentralen.hypersys.login

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RingesentralenPanacheEntity
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.Table

@RegisterForReflection
@Entity
@Table(name = "login_attempts")
data class LoginAttempt(
    var hypersysID: Int
) : RingesentralenPanacheEntity() {
    constructor() : this(
        hypersysID = 0
    )
}

@ApplicationScoped
class LoginAttemptRepository : PanacheRepositoryBase<LoginAttempt, Int>
