package no.roedt.kommune

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Cacheable
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.Hibernate

@Entity
@Table(name = "kommune")
@Cacheable
@RegisterForReflection
data class Kommune(
    var navn: String,
    @Id
    var nummer: String,
    var lokallag_id: Int?,
    var fylke_id: Int
) : PanacheEntityBase() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Kommune

        return nummer == other.nummer
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String = "Kommune(navn='$navn', nummer='$nummer', lokallag_id=$lokallag_id, fylke_id=$fylke_id)"
}

@ApplicationScoped
class KommuneRepository : PanacheRepositoryBase<Kommune, Int>
