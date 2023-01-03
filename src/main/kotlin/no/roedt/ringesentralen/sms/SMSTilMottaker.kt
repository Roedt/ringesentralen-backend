package no.roedt.ringesentralen.sms

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RoedtPanacheEntity
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
) : RoedtPanacheEntity()

@ApplicationScoped
class SMSTilMottakerRepository : PanacheRepositoryBase<SMSTilMottaker, Int>
