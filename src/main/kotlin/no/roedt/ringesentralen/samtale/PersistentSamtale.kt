package no.roedt.ringesentralen.samtale

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.Modus
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Table(name = "samtale")
@Entity
@RegisterForReflection
data class PersistentSamtale(
    var ringt: Int,
    var ringer: Int,
    var resultat: Int,
    var kommentar: String?,
    @Enumerated(EnumType.STRING) var modus: Modus
) : PanacheEntity() {
    constructor() : this(
        ringt = 0,
        ringer = 0,
        resultat=  0,
        kommentar = null,
        modus = Modus.velgere
    )
}

@ApplicationScoped
class PersistentSamtaleRepository : PanacheRepository<PersistentSamtale>