package no.roedt.ringesentralen.brukere

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.lokallag.LokallagRepository
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

    fun getFylke(lokallag: Int, postnummer: Int): Int =
        if (postnummer == -1 && lokallag != -1) getFylkeIdFraLokallag(lokallag) else toFylke(postnummer)

    fun getFylkeIdFraLokallag(lokallag: Int): Int = lokallagRepository.findById(lokallag.toLong()).fylke

    fun toFylke(postnummer: Int): Int =
        databaseUpdater.getResultList(
            "select fylke.id from `postnummer` p " +
                "inner join kommune kommune on p.KommuneKode = kommune.nummer " +
                "inner join `fylker` fylke on fylke.id=kommune.fylke_id where postnummer = $postnummer"
        )
            .map { it as Int }
            .firstOrNull()
            ?: -1
}
