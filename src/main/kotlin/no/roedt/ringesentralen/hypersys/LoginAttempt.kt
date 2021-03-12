package no.roedt.ringesentralen.hypersys

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.runtime.annotations.RegisterForReflection
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Table

@RegisterForReflection
@Entity
@Table(name = "login_attempts")
data class LoginAttempt(
    var hypersysID: Int,
    var datetime: LocalDateTime
) : PanacheEntity()