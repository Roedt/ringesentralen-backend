package no.roedt.ringesentralen.person

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.runtime.annotations.RegisterForReflection
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "person")
@Cacheable
@RegisterForReflection
data class Person(
        var hypersysID: Int?,
        var fornavn: String,
        var etternavn: String,
        var telefonnummer: String?,
        var email: String?,
        var postnummer: Int,
        var fylke: Int,
        var lokallag: Int,
        var groupID: Int,
) : PanacheEntity() {
    constructor() : this(
            hypersysID = null,
            fornavn = "",
            etternavn = "",
            telefonnummer = "",
            email = null,
            postnummer = 0,
            fylke = -1,
            lokallag = 1,
            groupID = 0
    )
}