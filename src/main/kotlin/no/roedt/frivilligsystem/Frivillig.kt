package no.roedt.frivilligsystem

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RoedtPanacheEntity
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
    var spraak: String?,
    var fortellLittOmDegSelv: String?,
    var registrertTidspunkt: Instant
) : RoedtPanacheEntity()

@ApplicationScoped
class FrivilligRepository : PanacheRepositoryBase<Frivillig, Int>
