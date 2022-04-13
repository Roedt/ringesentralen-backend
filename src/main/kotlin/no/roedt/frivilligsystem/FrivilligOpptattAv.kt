package no.roedt.frivilligsystem

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RoedtPanacheEntity
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
    var frivillig_id: Int,
    @Enumerated(EnumType.STRING) var opptattAv: OpptattAv
) : RoedtPanacheEntity() {
    constructor() : this(
        frivillig_id = 0,
        opptattAv = OpptattAv.Atasylsoekereskalfaajobbemensdeventerpaasvar
    )
}

@ApplicationScoped
class FrivilligOpptattAvRepository : PanacheRepositoryBase<FrivilligOpptattAv, Int>
