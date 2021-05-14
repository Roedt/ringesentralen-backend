package no.roedt.ringesentralen.samtale

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "oppfoelgingValg21")
@Entity
@RegisterForReflection
data class OppfoelgingValg21(
    var samtaleId: Int,
    var vilBliMerAktiv: Boolean,
    var vilPolitikkLink: Boolean,
    var vilIkkeBliRingt: Boolean,
    var vilHaMedlemsLink: Boolean,
    var vilHaFellesskapLink: Boolean,
    var vilBliRingtAugust: Boolean,
) : PanacheEntity() {
    constructor() : this(
        samtaleId = 0,
        vilBliMerAktiv = false,
        vilPolitikkLink = false,
        vilIkkeBliRingt = false,
        vilHaMedlemsLink = false,
        vilHaFellesskapLink = false,
        vilBliRingtAugust = false
    )
}

@ApplicationScoped
class OppfoelgingValg21Repository : PanacheRepository<OppfoelgingValg21>
