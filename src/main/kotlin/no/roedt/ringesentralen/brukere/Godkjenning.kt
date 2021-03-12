package no.roedt.ringesentralen.brukere

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.runtime.annotations.RegisterForReflection
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "Godkjenning")
@Entity
@RegisterForReflection
data class Godkjenning(
    var godkjenner: Int,
    var godkjentPerson: Int,
    var nyGroupId: Int
) : PanacheEntity()