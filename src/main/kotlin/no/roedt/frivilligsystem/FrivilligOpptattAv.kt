package no.roedt.frivilligsystem

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RoedtPanacheEntity
import org.hibernate.Hibernate
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Entity
@Table(name = "frivilligOpptattAv")
@Cacheable
@RegisterForReflection
data class FrivilligOpptattAv(
    var frivillig_id: Int,
    @Enumerated(EnumType.STRING) var opptattAv: OpptattAv
) : RoedtPanacheEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as FrivilligOpptattAv

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String = "FrivilligOpptattAv(id=$id, frivillig_id=$frivillig_id, opptattAv=$opptattAv)"
}

@ApplicationScoped
class FrivilligOpptattAvRepository : PanacheRepositoryBase<FrivilligOpptattAv, Int>
