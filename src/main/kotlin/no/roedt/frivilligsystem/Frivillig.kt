package no.roedt.frivilligsystem

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.frivilligsystem.registrer.ErMedlemStatus
import java.time.Instant
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Entity
@Table(name = "frivillig")
@Cacheable
@RegisterForReflection
data class Frivillig(
    var personId: Int,
    var alleredeAktivILokallag: Boolean,
    @Enumerated(EnumType.STRING)
    var medlemIRoedt: ErMedlemStatus,
    var spesiellKompetanse: String?,
    var andreTingDuVilBidraMed: String?,
    var fortellLittOmDegSelv: String?,
    var registrertTidspunkt: Instant
) : PanacheEntity()
{
    constructor() : this(
        personId = 0,
        alleredeAktivILokallag = false,
        medlemIRoedt = ErMedlemStatus.Nei,
        spesiellKompetanse = null,
        andreTingDuVilBidraMed = null,
        fortellLittOmDegSelv = null,
        Instant.MIN
    )
}

@ApplicationScoped
class FrivilligRepository : PanacheRepository<Frivillig>