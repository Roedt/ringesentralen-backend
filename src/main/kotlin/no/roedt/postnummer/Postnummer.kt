package no.roedt.postnummer

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Cacheable
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import no.roedt.kommune.Kommune
import org.hibernate.Hibernate

@Entity
@Table(name = "postnummer")
@Cacheable
@RegisterForReflection
data class Postnummer(
    @Id
    val Postnummer: String,
    val Poststed: String,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "KommuneKode", nullable = false)
    val KommuneKode: Kommune
) : PanacheEntityBase() {
    fun erUkjent() = Postnummer == "-1"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Postnummer

        return Postnummer == other.Postnummer
    }

    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String = "Postnummer(Postnummer='$Postnummer', Poststed='$Poststed')"
}

@ApplicationScoped
class PostnummerRepository : PanacheRepositoryBase<Postnummer, String>
