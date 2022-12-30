package no.roedt

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "kommune")
@Cacheable
@RegisterForReflection
data class Kommune(
    var navn: String,
    @Id
    var nummer: String,
    var lokallag_id: Int?,
    var fylke_id: Int
) : PanacheEntityBase() {
    constructor() : this(
        navn = "",
        nummer = "",
        lokallag_id = 0,
        fylke_id = 0
    )
}

@ApplicationScoped
class KommuneRepository : PanacheRepositoryBase<Kommune, Int>
