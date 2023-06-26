package no.roedt.frivilligsystem

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Cacheable
import jakarta.persistence.Entity
import jakarta.persistence.Table
import no.roedt.RoedtPanacheEntity
import org.hibernate.Hibernate

@Entity
@Table(name = "frivilligKorona")
@Cacheable
@RegisterForReflection
data class FrivilligKorona(
    var frivillig_id: Int,
    var haandtering: String,
    var personlig: Boolean,
    var tydelig: String,
    var forslag: String
) : RoedtPanacheEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as FrivilligKorona

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String =
        "FrivilligKorona(id=$id, frivillig_id=$frivillig_id, haandtering='$haandtering', personlig=$personlig, tydelig='$tydelig', forslag='$forslag')"
}

@ApplicationScoped
class FrivilligKoronaRepository : PanacheRepositoryBase<FrivilligKorona, Int>
