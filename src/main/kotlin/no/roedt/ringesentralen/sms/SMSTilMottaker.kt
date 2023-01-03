package no.roedt.ringesentralen.sms

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RoedtPanacheEntity
import org.hibernate.Hibernate
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

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
    override fun toString(): String =
        "SMSTilMottaker(id=$id, sms_id=$sms_id, mottaker_id=$mottaker_id, utsendingsstatus=$utsendingsstatus)"
}

@ApplicationScoped
class SMSTilMottakerRepository : PanacheRepositoryBase<SMSTilMottaker, Int>
