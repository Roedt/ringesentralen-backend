package no.roedt.ringesentralen.person

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RoedtPanacheEntity
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
) : RoedtPanacheEntity() {
    constructor() : this(
        oppretta = Instant.MIN,
        personId = 0
    )
}

@ApplicationScoped
class RingerRepository : PanacheRepositoryBase<Ringer, Int>
