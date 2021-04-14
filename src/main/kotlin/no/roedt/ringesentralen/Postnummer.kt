package no.roedt.ringesentralen

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
    var postnummer: Int,
    var poststed: String,
    var KommuneKode: Int
) : PanacheEntityBase() {
    constructor() : this(
        postnummer = 0,
        poststed = "",
        KommuneKode = 0
    )
}

@ApplicationScoped
class PostnummerRepository : PanacheRepositoryBase<Postnummer, Int>