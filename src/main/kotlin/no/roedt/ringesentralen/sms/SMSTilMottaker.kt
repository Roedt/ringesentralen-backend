package no.roedt.frivilligsystem.sms

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RingesentralenPanacheEntity
import no.roedt.ringesentralen.sms.Utsendingsstatus
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
) : RingesentralenPanacheEntity() {
    constructor() : this(
        sms_id = 0,
        mottaker_id = 0,
        utsendingsstatus = Utsendingsstatus.KlarTilUtsending
    )
}

@ApplicationScoped
class SMSTilMottakerRepository : PanacheRepositoryBase<SMSTilMottaker, Int>
