package no.roedt.brukere

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Cacheable
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import no.roedt.DatabaseUpdater
import no.roedt.lokallag.LokallagRepository
import no.roedt.person.Postnummer
import org.hibernate.Hibernate

@Entity
@Table(name = "fylker")
@Cacheable
@RegisterForReflection
data class Fylke(
    @Id
    var id: Int,
    var navn: String
) : PanacheEntityBase() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Fylke

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String = "Fylke(id=$id, navn='$navn')"
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
