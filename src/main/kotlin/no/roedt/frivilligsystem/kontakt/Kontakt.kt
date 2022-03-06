package no.roedt.frivilligsystem.kontakt

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RingesentralenPanacheEntity
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
    var registrert_av: Int,
    var datetime: Instant
) : RingesentralenPanacheEntity() {
    constructor() : this(
        frivillig_id = 0,
        tilbakemelding = "",
        registrert_av = 0,
        datetime = Instant.MIN
    )
}

@ApplicationScoped
class KontaktRepository : PanacheRepositoryBase<Kontakt, Int>
