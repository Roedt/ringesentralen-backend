package no.roedt.ringesentralen.samtale

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import no.roedt.RoedtPanacheEntity
import no.roedt.ringesentralen.Modus
import org.hibernate.Hibernate

@Table(name = "samtale")
@Entity
@RegisterForReflection
data class PersistentSamtale(
    var ringt: Int,
    var ringer: Int,
    var resultat: Int,
    var ringesesjon: Int,
    @Column(columnDefinition = "longtext")
    var kommentar: String?,
    @Enumerated(EnumType.STRING) var modus: Modus
) : RoedtPanacheEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as PersistentSamtale

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String =
        "PersistentSamtale(id=$id, ringt=$ringt, ringer=$ringer, resultat=$resultat, ringesesjon=$ringesesjon, kommentar=$kommentar, modus=$modus)"
}

@ApplicationScoped
class PersistentSamtaleRepository : PanacheRepositoryBase<PersistentSamtale, Int>
