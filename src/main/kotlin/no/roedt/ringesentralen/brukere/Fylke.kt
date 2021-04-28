package no.roedt.ringesentralen.brukere

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Kommune
import no.roedt.ringesentralen.KommuneRepository
import no.roedt.ringesentralen.PostnummerIKommunerMedFleireLag
import no.roedt.ringesentralen.PostnummerIKommunerMedFleireLagRepository
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
    private val kommuneRepository: KommuneRepository,
    private val postnummerIKommunerMedFleireLagRepository: PostnummerIKommunerMedFleireLagRepository,
    private val databaseUpdater: DatabaseUpdater
) : PanacheRepositoryBase<Fylke, Int> {

    fun getFylke(lokallag: Int, postnummer: Int): Int =
        if (postnummer == -1 && lokallag != -1) getFylkeFraLokallag(lokallag) else toFylke(postnummer)

    fun getFylkeFraLokallag(lokallag: Int): Int =
        kommuneRepository.find("lokallag_id=?1", lokallag).firstResultOptional<Kommune>()
            .map { it.fylke_id }
            .orElseGet {
                postnummerIKommunerMedFleireLagRepository.find("lokallag=?1", lokallag)
                    .firstResultOptional<PostnummerIKommunerMedFleireLag>()
                    .map { it.fylke }
                    .orElse(-1)
            }

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