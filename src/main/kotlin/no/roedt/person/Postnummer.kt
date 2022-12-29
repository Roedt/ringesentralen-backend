package no.roedt.person

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import io.quarkus.runtime.annotations.RegisterForReflection
import org.jboss.resteasy.spi.metadata.ResourceBuilder.constructor
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "postnummer")
@Cacheable
@RegisterForReflection
data class Postnummer(
    @Id
    val Postnummer: String
) : PanacheEntityBase() {
    fun erUkjent() = Postnummer != "-1"

    constructor() : this(
        Postnummer = ""
    )
}