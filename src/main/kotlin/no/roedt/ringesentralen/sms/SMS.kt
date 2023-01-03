package no.roedt.ringesentralen.sms

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RoedtPanacheEntity
import org.hibernate.Hibernate
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Table

@RegisterForReflection
@Entity
@Table(name = "sms")
@Cacheable
data class SMS(
    var tekst: String
) : RoedtPanacheEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as SMS

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String = "SMS(id=$id, tekst='$tekst')"
}

@ApplicationScoped
class SMSRepository : PanacheRepositoryBase<SMS, Int>
