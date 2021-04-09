package no.roedt.ringesentralen.samtale

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "samtale")
@Entity
@RegisterForReflection
data class PersistentSamtale(
    var ringt: Int,
    var ringer: Int,
    var resultat: Int,
    var kommentar: String?
) : PanacheEntity() {
    constructor() : this(
        ringt = 0,
        ringer = 0,
        resultat=  0,
        kommentar = null
    )
}

@ApplicationScoped
class PersistentSamtaleRepository : PanacheRepository<PersistentSamtale>