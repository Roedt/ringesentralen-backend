package no.roedt.frivilligsystem

import jakarta.enterprise.context.Dependent

@Dependent
class FrivilligKoronaService(internal val repository: FrivilligKoronaRepository) {
    fun slett(id: Int) = repository.delete("frivillig_id=?1", id)

    fun hent(id: Int): FrivilligKorona? =
        repository.find("frivillig_id", id)
            .firstResultOptional<FrivilligKorona>().orElse(null)

    fun persist(frivilligKorona: FrivilligKorona) = repository.persist(frivilligKorona)
}
