package no.roedt.ringesentralen.hypersys

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "ringer")
@Entity
@RegisterForReflection
data class Ringer(
    var personId: Int
) : PanacheEntity() {
    constructor() : this(
        personId = 0
    )
}


@ApplicationScoped
class RingerRepository : PanacheRepository<Ringer>