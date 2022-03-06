package no.roedt.frivilligsystem.sms

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RingesentralenPanacheEntity
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Table

@RegisterForReflection
@Entity
@Table(name = "sms")
@Cacheable
data class SMS(
    var tekst: String
) : RingesentralenPanacheEntity() {
    constructor() : this(
        tekst = ""
    )
}

@ApplicationScoped
class SMSRepository : PanacheRepositoryBase<SMS, Int>
