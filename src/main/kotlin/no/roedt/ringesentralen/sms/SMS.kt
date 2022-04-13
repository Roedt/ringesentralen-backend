package no.roedt.frivilligsystem.sms

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RoedtPanacheEntity
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
) : RoedtPanacheEntity() {
    constructor() : this(
        tekst = ""
    )
}

@ApplicationScoped
class SMSRepository : PanacheRepositoryBase<SMS, Int>
