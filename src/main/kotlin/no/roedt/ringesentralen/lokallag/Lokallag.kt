package no.roedt.ringesentralen.lokallag

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.DatabaseUpdater
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
    var fylke: Int
) : PanacheEntity() {
    constructor() : this(
        navn = "",
        hypersysID = 0,
        fylke = 0
    )
}

@ApplicationScoped
class LokallagRepository(
    private val databaseUpdater: DatabaseUpdater
) : PanacheRepository<Lokallag> {

    fun fromPostnummer(postnummer: Int): Int =
        toLokallagId("select lokallag from postnummerIKommunerMedFleireLag where postnummerFra <= $postnummer and postnummerTil >= $postnummer")
            ?: toLokallagId("select l.id from lokallag l inner join kommune k on k.lokallag_id = l.id inner join postnummer  p on p.kommunekode = k.nummer where p.postnummer = $postnummer")
            ?: -1

    private fun toLokallagId(query: String) = databaseUpdater.getResultList(query).map { it as Int }.firstOrNull()

    fun fromOrganisationName(organisationName: String): Int =
        find("navn", organisationName)
            .firstResultOptional<Lokallag>()
            ?.map { it.id }
            ?.map { it.toInt() }
            ?.orElse(-1)
            ?: -1

    fun fromFylke(fylkeId: Int): List<Lokallag> = list("fylke=?1", fylkeId)
}
