package no.roedt.ringesentralen.samtale

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.Table
import no.roedt.RoedtPanacheEntity
import org.hibernate.Hibernate

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
    var vilBliRingtAugust: Boolean
) : RoedtPanacheEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as OppfoelgingValg21

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String =
        "OppfoelgingValg21(id=$id, " +
            "samtaleId=$samtaleId, " +
            "vilBliMerAktiv=$vilBliMerAktiv, " +
            "vilPolitikkLink=$vilPolitikkLink, " +
            "vilIkkeBliRingt=$vilIkkeBliRingt, " +
            "vilHaMedlemsLink=$vilHaMedlemsLink, " +
            "vilHaFellesskapLink=$vilHaFellesskapLink, " +
            "vilBliRingtAugust=$vilBliRingtAugust)"
}

@ApplicationScoped
class OppfoelgingValg21Repository : PanacheRepositoryBase<OppfoelgingValg21, Int>
