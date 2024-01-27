package no.roedt.hypersys.login

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.Table
import no.roedt.RoedtPanacheEntity
import org.hibernate.Hibernate

@RegisterForReflection
@Entity
@Table(name = "login_attempts")
data class LoginAttempt(
    var hypersysID: Int
) : RoedtPanacheEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as LoginAttempt

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String = "LoginAttempt(id=$id, hypersysID=$hypersysID)"
}

@ApplicationScoped
class LoginAttemptRepository : PanacheRepositoryBase<LoginAttempt, Int>
