package no.roedt.frivilligsystem.registrer

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.frivilligsystem.Frivillig
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
    var frivillig: Frivillig,
    @Enumerated(EnumType.STRING) var aktivitet: Aktivitet
) : PanacheEntity() {
    constructor() : this(
        Frivillig(),
        Aktivitet.Bil
    )
}

@ApplicationScoped
class AktivitetForFrivilligRepository : PanacheRepository<AktivitetForFrivillig>