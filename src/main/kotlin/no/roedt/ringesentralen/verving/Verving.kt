package no.roedt.ringesentralen.verving

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RoedtPanacheEntity
import javax.enterprise.context.Dependent
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "verving")
@Cacheable
@RegisterForReflection
data class Verving(
    var telefonnummer: String,
    var fornavn: String,
    var etternavn: String,
    var postnummer: Int,
    var verversNavn: String?
) : RoedtPanacheEntity() {
    constructor() : this(
        telefonnummer = "",
        fornavn = "",
        etternavn = "",
        postnummer = 0,
        verversNavn = null
    )
}

@Dependent
class VervingRepository : PanacheRepositoryBase<Verving, Int>
