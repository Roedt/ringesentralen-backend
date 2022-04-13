package no.roedt.person

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.Kilde
import no.roedt.RingesentralenPanacheEntity
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
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
    private var groupID: Int,
    @Enumerated(EnumType.STRING) var kilde: Kilde,
    var iperID: Int?
) : RingesentralenPanacheEntity() {
    fun isSystembruker(): Boolean = fornavn == "Systembruker" && etternavn == "Frontend"

    constructor() : this(
        hypersysID = null,
        fornavn = "",
        etternavn = "",
        telefonnummer = "",
        email = null,
        postnummer = 0,
        fylke = -1,
        groupID = 0,
        lokallag = 1,
        kilde = Kilde.Verva,
        iperID = null
    )

    fun groupID() = groupID

    fun setGroupID(groupID: Int) {
        if (RingesentralenGroupID.isBrukerEllerVenter(this.groupID) && RingesentralenGroupID.isIkkeRegistrertRinger(groupID)) {
            throw RuntimeException("$id er allerede ringer, og kan ikke settes til ei ikke-ringer-rolle.")
        }
        this.groupID = groupID
    }

    fun setGroupID(groupID: GroupID) = setGroupID(groupID.nr)
}
