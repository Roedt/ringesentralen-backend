package no.roedt.lokallag

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Cacheable
import jakarta.persistence.Entity
import jakarta.persistence.Table
import no.roedt.RoedtPanacheEntity
import no.roedt.list
import no.roedt.postnummer.Postnummer
import org.hibernate.Hibernate
import java.time.Instant

@Entity
@Table(name = "lokallag")
@Cacheable
@RegisterForReflection
data class Lokallag(
    var navn: String,
    var hypersysID: Int?,
    var fylke: Int,
    var sistOppdatert: Instant?
) : RoedtPanacheEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Lokallag

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String =
        "Lokallag(id=$id, navn='$navn', hypersysID=$hypersysID, fylke=$fylke, sistOppdatert=$sistOppdatert)"
}

@ApplicationScoped
class LokallagRepository : PanacheRepositoryBase<Lokallag, Int> {

    fun fromPostnummer(postnummer: Postnummer): Int =
        toLokallagId("select lokallag from postnummerIKommunerMedFleireLag where postnummerFra <= ${postnummer.Postnummer.toInt()} and postnummerTil >= ${postnummer.Postnummer.toInt()}")
            ?: toLokallagId("select l.id from lokallag l inner join kommune k on k.lokallag_id = l.id inner join postnummer p on p.kommunekode = k.nummer where p.postnummer = '${postnummer.Postnummer.toInt()}'")
            ?: -1

    private fun toLokallagId(query: String) = entityManager.list(query).map { it as Int }.firstOrNull()

    fun fromOrganisationName(organisationName: String): Int {
        val finnLokallag = finnLokallag(organisationName)
        if (finnLokallag?.isPresent == true) {
            return finnLokallag.get()
        }
        val medRoedtprefiks = finnLokallag("Rødt $organisationName")
        if (medRoedtprefiks?.isPresent == true) {
            println("$organisationName fins i databasen utan Rødt-prefiks, og bør oppdaterast")
            return medRoedtprefiks.get()
        }
        println("$organisationName fins ikkje som lokallag i databasen, returnerer udefinert")
        return -1
    }

    private fun finnLokallag(organisationName: String) =
        find("navn", organisationName)
            .firstResultOptional<Lokallag>()
            ?.map { it.id }
            ?.map { it.toInt() }

    fun fromFylke(fylkeId: Int): List<Lokallag> = list("fylke=?1", fylkeId)
}
