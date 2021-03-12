package no.roedt.ringesentralen.samtale

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "oppfoelgingKorona")
@Entity
@RegisterForReflection
data class OppfoelgingKorona(
    var personId: Int,
    var koronaprogram: Boolean,
    var merAktiv: Boolean,
    var valgkampsbrev: Boolean,
    var vilIkkeBliRingt: Boolean
) : PanacheEntity()

@ApplicationScoped
class OppfoelgingKoronaRepository : PanacheRepository<OppfoelgingKorona>