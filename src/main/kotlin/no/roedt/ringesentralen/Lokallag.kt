package no.roedt.ringesentralen

import io.quarkus.hibernate.orm.panache.PanacheEntity
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "lokallag")
@Cacheable
data class Lokallag(
        var name: String
): PanacheEntity() {
    constructor() : this("")
}
