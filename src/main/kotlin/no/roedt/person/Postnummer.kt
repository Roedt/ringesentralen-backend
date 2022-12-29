package no.roedt.person

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "postnummer")
@Cacheable
@RegisterForReflection
data class Postnummer(
    @Id
    val Postnummer: String,
    val Poststed: String,
    val KommuneKode: Int
) : PanacheEntityBase() {
    fun erUkjent() = Postnummer != "-1"

    constructor() : this(
        Postnummer = "",
        Poststed = "",
        KommuneKode = 0
    )
}

@ApplicationScoped
class PostnummerRepository : PanacheRepositoryBase<Postnummer, String> {
    fun ukjent(): Postnummer = findById("-1")
}
