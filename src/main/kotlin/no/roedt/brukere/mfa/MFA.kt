package no.roedt.brukere.mfa

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.Dependent
import jakarta.persistence.Cacheable
import jakarta.persistence.Entity
import jakarta.persistence.Table
import no.roedt.RoedtPanacheEntity
import org.hibernate.Hibernate

@Table(name = "mfa")
@Entity
@Cacheable
@RegisterForReflection
data class MFA(
    var engangskode: String,
    var verifisert: Boolean,
    var epost: String,
    var enhetsid: String
) : RoedtPanacheEntity() {

    companion object {
        fun generer(): String {
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..6)
                .map { allowedChars.random() }
                .joinToString("")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as MFA

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String =
        "MFA(id=$id, engangskode='$engangskode', verifisert=$verifisert, epost='$epost', enhetsid='$enhetsid')"
}

@Dependent
class MFARepository : PanacheRepositoryBase<MFA, Int> {
    private fun erLagraPaaDenneEnheten(mfaRequest: DekryptertMFARequest) =
        find("epost=?1 and enhetsid=?2 and verifisert=?3", mfaRequest.brukernavn, mfaRequest.enhetsid, true).count() > 0

    fun erVerifisert(mfaRequest: DekryptertMFARequest) =
        find("verifisert=?1 and epost=?2 and enhetsid=?3", true, mfaRequest.brukernavn, mfaRequest.enhetsid).count() > 0

    fun matcher(request: SettVerifisertRequest) =
        find(
            "epost=?1 and enhetsid=?2 and engangskode=?3",
            request.brukarnamn,
            request.enhetsid,
            request.engangskode
        ).count() > 0

    fun settVerifisert(request: SettVerifisertRequest) {
        update(
            "verifisert=true where epost=?1 and enhetsid=?2 and engangskode=?3",
            request.brukarnamn,
            request.enhetsid,
            request.engangskode
        )
    }

    fun lagre(mfa: MFA) {
        val erLagra = erLagraPaaDenneEnheten(DekryptertMFARequest(enhetsid = mfa.enhetsid, brukernavn = mfa.epost))
        if (erLagra) {
            return
        }
        delete("epost=?1 and enhetsid=?2 and verifisert=?3", mfa.epost, mfa.enhetsid, false)
        persist(mfa)
    }
}

@RegisterForReflection
data class SettVerifisertRequest(
    val brukarnamn: String,
    val enhetsid: String,
    val engangskode: String
)
