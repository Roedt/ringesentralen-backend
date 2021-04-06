package no.roedt.ringesentralen.verving

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
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
    var ververID: String?
) : PanacheEntity() {
    constructor(): this(
        telefonnummer = "",
        fornavn = "",
        etternavn = "",
        postnummer = 0,
        ververID = null
    )
}

@Dependent
class VervingRepository: PanacheRepository<Verving>