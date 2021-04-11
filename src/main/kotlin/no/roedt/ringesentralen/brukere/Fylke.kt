package no.roedt.ringesentralen.brukere

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
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
    private val postnummerIKommunerMedFleireLagRepository: PostnummerIKommunerMedFleireLagRepository
)  : PanacheRepositoryBase<Fylke, Int> {

    fun getFylkeFraLokallag(lokallag: Int): Int =
        kommuneRepository.find("lokallag_id=?1", lokallag).firstResultOptional<Kommune>()
            .map { it.fylke_id }
            .orElseGet {
                postnummerIKommunerMedFleireLagRepository.find("lokallag=?1", lokallag)
                    .firstResultOptional<PostnummerIKommunerMedFleireLag>()
                    .map { it.fylke }
                    .orElse(-1)
            }
}