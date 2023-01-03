package no.roedt.ringesentralen.verving

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RoedtPanacheEntity
import org.hibernate.Hibernate
import javax.enterprise.context.Dependent
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "verving")
@Cacheable
@RegisterForReflection
data class Verving(
    var telefonnummer: String,
    var fornavn: String,
    var etternavn: String,
    var postnummer: String,
    var verversNavn: String?
) : RoedtPanacheEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Verving

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String =
        "Verving(id=$id, telefonnummer='$telefonnummer', fornavn='$fornavn', etternavn='$etternavn', postnummer='$postnummer', verversNavn=$verversNavn)"
}

@Dependent
class VervingRepository : PanacheRepositoryBase<Verving, Int>
