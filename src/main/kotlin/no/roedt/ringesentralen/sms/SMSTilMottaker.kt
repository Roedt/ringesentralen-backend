package no.roedt.ringesentralen.sms

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Cacheable
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import no.roedt.RoedtPanacheEntity
import org.hibernate.Hibernate

@RegisterForReflection
@Entity
@Table(name = "smsTilMottaker")
@Cacheable
data class SMSTilMottaker(
    var sms_id: Int,
    var mottaker_id: Int,
    @Enumerated(EnumType.STRING) var utsendingsstatus: Utsendingsstatus
) : RoedtPanacheEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as SMSTilMottaker

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String = "SMSTilMottaker(id=$id, sms_id=$sms_id, mottaker_id=$mottaker_id, utsendingsstatus=$utsendingsstatus)"
}

@ApplicationScoped
class SMSTilMottakerRepository : PanacheRepositoryBase<SMSTilMottaker, Int>
