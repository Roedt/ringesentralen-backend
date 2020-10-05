package no.roedt.ringesentralen.samtale

import io.quarkus.hibernate.orm.panache.PanacheEntity
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.NamedQuery
import javax.persistence.Table

@Entity
@Table(name = "person")
@Cacheable
data class RingbarPerson(
        var givenName: String,
        var familyName: String,
        var phone: String,
        var lastCall: Int,
        var nameEnlister: String?,
        var postnumber: String,
        var countyID: Int,
        var lokallag: Int
) : PanacheEntity() {
    constructor() : this(
            givenName = "",
            familyName = "",
            phone = "",
            lastCall = 0,
            nameEnlister = null,
            postnumber = "0",
            countyID = -1,
            lokallag = 1
    )
}