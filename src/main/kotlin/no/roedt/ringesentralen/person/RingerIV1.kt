package no.roedt.ringesentralen.person

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "ringerIV1")
@Entity
@RegisterForReflection
data class RingerIV1(
    var telefonnummer: String,
    var brukergruppe: Int
) : PanacheEntity()

@ApplicationScoped
class RingerIV1Repository : PanacheRepository<RingerIV1>