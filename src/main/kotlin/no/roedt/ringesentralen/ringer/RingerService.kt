package no.roedt.ringesentralen.ringer

import io.quarkus.hibernate.orm.panache.PanacheQuery
import jakarta.enterprise.context.Dependent

@Dependent
class RingerService(private val repository: RingerRepository) {
    fun finnFraPerson(personId: Int): PanacheQuery<Ringer> = repository.find("personId", personId)
    fun slett(personId: Int) = repository.delete("personId=?1", personId)
    fun persist(ringer: Ringer) = repository.persist(ringer)
}
