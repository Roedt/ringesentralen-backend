package no.roedt.frivilligsystem.sms

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
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
    var sms_id: Long,
    var mottaker_id: Long,
    @Enumerated(EnumType.STRING) var utsendingsstatus: Utsendingsstatus
) : PanacheEntity() {
    constructor() : this(
        sms_id = 0L,
        mottaker_id = 0L,
        utsendingsstatus = Utsendingsstatus.KlarTilUtsending
    )
}

@ApplicationScoped
class SMSTilMottakerRepository : PanacheRepository<SMSTilMottaker>
