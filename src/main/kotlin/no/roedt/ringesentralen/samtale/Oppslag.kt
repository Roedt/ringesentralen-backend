package no.roedt.ringesentralen.samtale

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "oppslag")
@Entity
@RegisterForReflection
data class Oppslag(
    var ringt: Int,
    var ringerHypersysId: Int
) : PanacheEntity()

@ApplicationScoped
class OppslagRepository : PanacheRepository<Oppslag>