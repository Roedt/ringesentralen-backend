package no.roedt.lokallag

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.RoedtPanacheEntity
import no.roedt.DatabaseUpdater
import java.time.Instant
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Table

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
    constructor() : this(
        navn = "",
        hypersysID = 0,
        fylke = 0,
        sistOppdatert = Instant.now()
    )
}

@ApplicationScoped
class LokallagRepository(
    private val databaseUpdater: DatabaseUpdater
) : PanacheRepositoryBase<Lokallag, Int> {

    fun fromPostnummer(postnummer: Int): Int =
        toLokallagId("select lokallag from postnummerIKommunerMedFleireLag where postnummerFra <= $postnummer and postnummerTil >= $postnummer")
            ?: toLokallagId("select l.id from lokallag l inner join kommune k on k.lokallag_id = l.id inner join postnummer  p on p.kommunekode = k.nummer where p.postnummer = $postnummer")
            ?: -1

    private fun toLokallagId(query: String) = databaseUpdater.getResultList(query).map { it as Int }.firstOrNull()

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
