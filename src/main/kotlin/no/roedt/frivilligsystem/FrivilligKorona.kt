package no.roedt.frivilligsystem

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RoedtPanacheEntity
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "frivilligKorona")
@Cacheable
@RegisterForReflection
data class FrivilligKorona(
    var frivillig_id: Int,
    var haandtering: String,
    var personlig: Boolean,
    var tydelig: String,
    var forslag: String
) : RoedtPanacheEntity()

@ApplicationScoped
class FrivilligKoronaRepository : PanacheRepositoryBase<FrivilligKorona, Int>
