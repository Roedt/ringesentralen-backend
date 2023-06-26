package no.roedt.frivilligsystem.kontakt

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Cacheable
import jakarta.persistence.Entity
import jakarta.persistence.Table
import no.roedt.RoedtPanacheEntity
import org.hibernate.Hibernate
import java.time.Instant

@RegisterForReflection
@Entity
@Table(name = "kontakt")
@Cacheable
data class Kontakt(
    var frivillig_id: Int,
    var tilbakemelding: String,
    var registrert_av: Int,
    var datetime: Instant
) : RoedtPanacheEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Kontakt

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String =
        "Kontakt(id=$id, frivillig_id=$frivillig_id, tilbakemelding='$tilbakemelding', registrert_av=$registrert_av, datetime=$datetime)"
}

@ApplicationScoped
class KontaktRepository : PanacheRepositoryBase<Kontakt, Int>
