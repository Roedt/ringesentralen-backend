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
@Table(name = "frivilligOpptattAv")
@Cacheable
@RegisterForReflection
data class FrivilligOpptattAv(
    var frivillig_id: Long,
    @Enumerated(EnumType.STRING) var opptattAv: OpptattAv
) : PanacheEntity() {
    constructor() : this(
        frivillig_id = 0L,
        opptattAv = OpptattAv.Atasylsoekereskalfaajobbemensdeventerpaasvar
    )
}

@ApplicationScoped
class FrivilligOpptattAvRepository : PanacheRepository<FrivilligOpptattAv>