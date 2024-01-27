package no.roedt.person

import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.persistence.Basic
import jakarta.persistence.Cacheable
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import no.roedt.Kilde
import no.roedt.RoedtPanacheEntity
import no.roedt.brukere.GroupID
import no.roedt.postnummer.Postnummer
import no.roedt.ringesentralen.brukere.RingesentralenGroupID
import org.hibernate.Hibernate
import java.time.Instant

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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "postnummer", nullable = false)
    var postnummer: Postnummer,
    var fylke: Int,
    var lokallag: Int,
    private var groupID: Int,
    // TODO: Enumerated type number/ordinal
    @Enumerated(EnumType.STRING) var kilde: Kilde,
    @Basic
    var sistOppdatert: Instant?
) : RoedtPanacheEntity() {
    fun isSystembruker(): Boolean = fornavn == "Systembruker" && etternavn == "Frontend"

    fun groupID() = groupID

    private fun setGroupID(groupID: Int) {
        if (RingesentralenGroupID.isBrukerEllerVenter(this.groupID) &&
            RingesentralenGroupID.isIkkeRegistrertRinger(
                groupID
            )
        ) {
            throw RuntimeException("$id er allerede ringer, og kan ikke settes til ei ikke-ringer-rolle.")
        }
        this.groupID = groupID
    }

    fun setGroupID(groupID: GroupID) = setGroupID(groupID.nr)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Person

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String =
        "Person(id=$id, " +
            "hypersysID=$hypersysID, " +
            "fornavn='$fornavn', " +
            "etternavn='$etternavn', " +
            "telefonnummer=$telefonnummer, " +
            "email=$email, " +
            "fylke=$fylke, " +
            "lokallag=$lokallag, " +
            "groupID=$groupID, " +
            "kilde=$kilde, " +
            "sistOppdatert=$sistOppdatert)"
}
