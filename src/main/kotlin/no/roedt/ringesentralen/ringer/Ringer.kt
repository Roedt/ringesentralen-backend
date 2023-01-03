package no.roedt.ringesentralen.ringer

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RoedtPanacheEntity
import org.hibernate.Hibernate
import java.time.Instant
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "ringer")
@Entity
@RegisterForReflection
data class Ringer(
    var oppretta: Instant,
    var personId: Int
) : RoedtPanacheEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Ringer

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String = "Ringer(id=$id, oppretta=$oppretta, personId=$personId)"
}

@ApplicationScoped
class RingerRepository : PanacheRepositoryBase<Ringer, Int>
