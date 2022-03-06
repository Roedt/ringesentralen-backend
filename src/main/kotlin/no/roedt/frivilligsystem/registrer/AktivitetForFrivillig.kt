package no.roedt.frivilligsystem.registrer

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RingesentralenPanacheEntity
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Entity
@Table(name = "aktivitetForFrivillig")
@Cacheable
@RegisterForReflection
data class AktivitetForFrivillig(
    var frivillig_id: Int,
    @Enumerated(EnumType.STRING) var aktivitet: Aktivitet
) : RingesentralenPanacheEntity() {
    constructor() : this(
        0,
        Aktivitet.Bil
    )
}

@ApplicationScoped
class AktivitetForFrivilligRepository : PanacheRepositoryBase<AktivitetForFrivillig, Int>
