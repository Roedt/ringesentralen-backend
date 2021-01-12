package no.roedt.ringesentralen.samtale

import io.quarkus.hibernate.orm.panache.PanacheEntity
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "person")
@Cacheable
data class RingbarPerson(
        var hypersysID: Int?,
        var givenName: String,
        var familyName: String,
        var phone: String,
        var lastCall: Int,
        var email: String?,
        var nameEnlister: String?,
        var postnumber: String,
        var countyID: Int,
        var lokallag: Int
) : PanacheEntity() {
    constructor() : this(
            hypersysID = null,
            givenName = "",
            familyName = "",
            phone = "",
            lastCall = 0,
            email = null,
            nameEnlister = null,
            postnumber = "0",
            countyID = -1,
            lokallag = 1
    )
}