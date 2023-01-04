package no.roedt.person

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.Kommune
import org.hibernate.Hibernate
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

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
class PostnummerRepository : PanacheRepositoryBase<Postnummer, String> {
    fun ukjent(): Postnummer = findById("-1")
}
