package no.roedt.brukere

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.DatabaseUpdater
import no.roedt.lokallag.LokallagRepository
import no.roedt.person.Postnummer
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "fylker")
@Cacheable
@RegisterForReflection
data class Fylke(
    @Id
    var id: Int,
    var navn: String
) : PanacheEntityBase() {
    constructor() : this(
        id = 0,
        navn = ""
    )
}

@ApplicationScoped
class FylkeRepository(
    private val databaseUpdater: DatabaseUpdater,
    private val lokallagRepository: LokallagRepository
) : PanacheRepositoryBase<Fylke, Int> {

    fun getFylke(lokallag: Int, postnummer: Postnummer): Int =
        if (!postnummer.erUkjent() && lokallag != -1) getFylkeIdFraLokallag(lokallag) else toFylke(postnummer)

    fun getFylkeIdFraLokallag(lokallag: Int): Int = lokallagRepository.findById(lokallag).fylke

    fun toFylke(postnummer: Postnummer): Int =
        databaseUpdater.getResultList(
            "select fylke.id from `postnummer` p " +
                "inner join kommune kommune on p.KommuneKode = kommune.nummer " +
                "inner join `fylker` fylke on fylke.id=kommune.fylke_id where postnummer = ${postnummer.Postnummer}"
        )
            .map { it as Int }
            .firstOrNull()
            ?: -1
}
