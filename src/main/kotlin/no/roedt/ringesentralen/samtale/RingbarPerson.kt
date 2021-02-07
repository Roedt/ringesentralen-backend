package no.roedt.ringesentralen.samtale

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.runtime.annotations.RegisterForReflection
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "person")
@Cacheable
@RegisterForReflection
data class RingbarPerson(
        var hypersysID: Int?,
        var fornavn: String,
        var etternavn: String,
        var phone: String,
        var lastCall: Int,
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
            phone = "",
            lastCall = 0,
            email = null,
            postnummer = 0,
            fylke = -1,
            lokallag = 1,
            groupID = 0
    )
}