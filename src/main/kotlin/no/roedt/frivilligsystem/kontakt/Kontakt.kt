package no.roedt.frivilligsystem.kontakt

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.runtime.annotations.RegisterForReflection
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
    var registrert_av: Long
) : PanacheEntity()