package no.roedt.ringesentralen.person

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
import java.time.Instant
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "ringer")
@Entity
@RegisterForReflection
data class Ringer(
    var oppretta: Instant,
    var personId: Int
) : PanacheEntity() {
    constructor() : this(
        oppretta = Instant.MIN,
        personId = 0
    )
}

@ApplicationScoped
class RingerRepository : PanacheRepository<Ringer>
