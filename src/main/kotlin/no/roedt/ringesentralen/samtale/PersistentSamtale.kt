package no.roedt.ringesentralen.samtale

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.runtime.annotations.RegisterForReflection
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "samtale")
@Entity
@RegisterForReflection
data class PersistentSamtale(
    var ringtId: Int,
    var ringerId: Int,
    var resultat: Int,
    var kommentar: String
) : PanacheEntity()