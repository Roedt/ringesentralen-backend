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
        var givenName: String,
        var familyName: String,
        var phone: String,
        var lastCall: Int,
        var email: String?,
        var postnummer: Int,
        var countyID: Int,
        var lokallag: Int,
        var groupID: Int,
) : PanacheEntity() {
    constructor() : this(
            hypersysID = null,
            givenName = "",
            familyName = "",
            phone = "",
            lastCall = 0,
            email = null,
            postnummer = 0,
            countyID = -1,
            lokallag = 1,
            groupID = 0
    )
}