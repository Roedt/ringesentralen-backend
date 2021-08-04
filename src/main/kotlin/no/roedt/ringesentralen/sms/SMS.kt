package no.roedt.frivilligsystem.sms

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
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
) : PanacheEntity() {
    constructor() : this(
        tekst = ""
    )
}

@ApplicationScoped
class SMSRepository : PanacheRepository<SMS>
