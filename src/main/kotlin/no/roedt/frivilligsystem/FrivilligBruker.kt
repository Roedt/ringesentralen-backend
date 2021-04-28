package no.roedt.frivilligsystem

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
@Table(name = "frivilligBruker")
@Cacheable
@RegisterForReflection
class FrivilligBruker(
    var personHypersysId: Int,
    @Enumerated(EnumType.STRING)
    var rolle: Rolle
) : PanacheEntity() {
    constructor() : this(
        0,
        Rolle.lokallag
    )
}

@ApplicationScoped
class FrivilligBrukerRepository : PanacheRepository<FrivilligBruker>