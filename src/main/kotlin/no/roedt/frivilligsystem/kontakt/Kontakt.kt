package no.roedt.frivilligsystem.kontakt

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
import java.time.Instant
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Table

@RegisterForReflection
@Entity
@Table(name = "kontakt")
@Cacheable
data class Kontakt(
    var frivillig_id: Int,
    var tilbakemelding: String,
    var registrert_av: Long,
    var datetime: Instant
) : PanacheEntity() {
    constructor() : this(
        frivillig_id = 0,
        tilbakemelding = "",
        registrert_av = 0L,
        datetime = Instant.MIN
    )
}

@ApplicationScoped
class KontaktRepository : PanacheRepository<Kontakt>
