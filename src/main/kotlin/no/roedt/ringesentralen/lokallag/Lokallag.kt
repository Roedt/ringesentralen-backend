package no.roedt.ringesentralen.lokallag

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "lokallag")
@Cacheable
@RegisterForReflection
data class Lokallag(
        var navn: String,
        var hypersysID: Int?
): PanacheEntity() {
    constructor() : this(
        navn = "",
        hypersysID = 0
    )
}

@ApplicationScoped
class LokallagRepository : PanacheRepository<Lokallag>