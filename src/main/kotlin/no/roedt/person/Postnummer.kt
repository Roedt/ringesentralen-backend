package no.roedt.person

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.Kommune
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
    fun erUkjent() = Postnummer != "-1"
}

@ApplicationScoped
class PostnummerRepository : PanacheRepositoryBase<Postnummer, String> {
    fun ukjent(): Postnummer = findById("-1")
}
