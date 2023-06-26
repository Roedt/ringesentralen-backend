package no.roedt.frivilligsystem.registrer

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Cacheable
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import no.roedt.RoedtPanacheEntity
import org.hibernate.Hibernate

@Entity
@Table(name = "aktivitetForFrivillig")
@Cacheable
@RegisterForReflection
data class AktivitetForFrivillig(
    var frivillig_id: Int,
    @Enumerated(EnumType.STRING) var aktivitet: Aktivitet
) : RoedtPanacheEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as AktivitetForFrivillig

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String = "AktivitetForFrivillig(id=$id, frivillig_id=$frivillig_id, aktivitet=$aktivitet)"
}

@ApplicationScoped
class AktivitetForFrivilligRepository : PanacheRepositoryBase<AktivitetForFrivillig, Int>
