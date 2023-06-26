package no.roedt.brukere

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.Table
import no.roedt.RoedtPanacheEntity
import org.hibernate.Hibernate

@Table(name = "godkjenning")
@Entity
@RegisterForReflection
data class Godkjenning(
    var godkjenner: Int,
    var godkjentPerson: Int,
    var nyGroupId: Int
) : RoedtPanacheEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Godkjenning

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String =
        "Godkjenning(id=$id, godkjenner=$godkjenner, godkjentPerson=$godkjentPerson, nyGroupId=$nyGroupId)"
}

@ApplicationScoped
class GodkjenningRepository : PanacheRepositoryBase<Godkjenning, Int>
