package no.roedt.frivilligsystem

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "frivilligKorona")
@Cacheable
@RegisterForReflection
data class FrivilligKorona(
    var frivillig_id: Long,
    var haandtering: String,
    var personlig: Boolean,
    var tydelig: String,
    var forslag: String
) : PanacheEntity() {
    constructor() : this(
        frivillig_id = 0L,
        haandtering = "",
        personlig = true,
        tydelig = "",
        forslag = ""
    )
}

@ApplicationScoped
class FrivilligKoronaRepository : PanacheRepository<FrivilligKorona>