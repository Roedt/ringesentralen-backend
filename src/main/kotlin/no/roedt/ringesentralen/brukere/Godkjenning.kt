package no.roedt.ringesentralen.brukere

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RingesentralenPanacheEntity
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "godkjenning")
@Entity
@RegisterForReflection
data class Godkjenning(
    var godkjenner: Int,
    var godkjentPerson: Int,
    var nyGroupId: Int
) : RingesentralenPanacheEntity() {
    constructor() : this(
        godkjenner = 0,
        godkjentPerson = 0,
        nyGroupId = 0
    )
}

@ApplicationScoped
class GodkjenningRepository : PanacheRepositoryBase<Godkjenning, Int>
