package no.roedt.frivilligsystem.registrer

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
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
    var frivillig_id: Long,
    @Enumerated(EnumType.STRING) var aktivitet: Aktivitet
) : PanacheEntity() {
    constructor() : this(
        0L,
        Aktivitet.Bil
    )
}

@ApplicationScoped
class AktivitetForFrivilligRepository : PanacheRepository<AktivitetForFrivillig>
