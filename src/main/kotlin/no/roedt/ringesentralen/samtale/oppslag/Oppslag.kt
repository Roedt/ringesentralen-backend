package no.roedt.ringesentralen.samtale.oppslag

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.Table
import no.roedt.RoedtPanacheEntity
import org.hibernate.Hibernate

@Table(name = "oppslag")
@Entity
@RegisterForReflection
data class Oppslag(
    var ringt: Int,
    var ringerHypersysId: Int
) : RoedtPanacheEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Oppslag

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String = "Oppslag(id=$id, ringt=$ringt, ringerHypersysId=$ringerHypersysId)"
}

@ApplicationScoped
class OppslagRepository : PanacheRepositoryBase<Oppslag, Int>
