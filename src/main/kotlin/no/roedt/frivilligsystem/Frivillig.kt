package no.roedt.frivilligsystem

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Cacheable
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import no.roedt.RoedtPanacheEntity
import no.roedt.frivilligsystem.registrer.ErMedlemStatus
import org.hibernate.Hibernate
import java.time.Instant

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
) : RoedtPanacheEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Frivillig

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String =
        "Frivillig(id=$id, personId=$personId, alleredeAktivILokallag=$alleredeAktivILokallag, medlemIRoedt=$medlemIRoedt, spesiellKompetanse=$spesiellKompetanse, andreTingDuVilBidraMed=$andreTingDuVilBidraMed, spraak=$spraak, fortellLittOmDegSelv=$fortellLittOmDegSelv, registrertTidspunkt=$registrertTidspunkt)"
}

@ApplicationScoped
class FrivilligRepository : PanacheRepositoryBase<Frivillig, Int>
