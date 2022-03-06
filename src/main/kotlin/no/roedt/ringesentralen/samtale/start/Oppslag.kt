package no.roedt.ringesentralen.samtale.start

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RingesentralenPanacheEntity
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "oppslag")
@Entity
@RegisterForReflection
data class Oppslag(
    var ringt: Int,
    var ringerHypersysId: Int
) : RingesentralenPanacheEntity() {
    constructor() : this(
        ringt = 0,
        ringerHypersysId = 0
    )
}

@ApplicationScoped
class OppslagRepository : PanacheRepositoryBase<Oppslag, Int>
