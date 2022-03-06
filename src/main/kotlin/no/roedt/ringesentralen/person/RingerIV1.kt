package no.roedt.ringesentralen.person

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RingesentralenPanacheEntity
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "ringerIV1")
@Entity
@RegisterForReflection
data class RingerIV1(
    var telefonnummer: String,
    var brukergruppe: Int
) : RingesentralenPanacheEntity() {
    constructor() : this(
        telefonnummer = "",
        brukergruppe = 0
    )
}

@ApplicationScoped
class RingerIV1Repository : PanacheRepositoryBase<RingerIV1, Int>
