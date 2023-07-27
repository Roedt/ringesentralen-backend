package no.roedt.frivilligsystem

import jakarta.enterprise.context.Dependent

@Dependent
class FrivilligOpptattAvService(internal val repository: FrivilligOpptattAvRepository) {
    fun hent(id: Int): List<String> = repository.list("frivillig_id", id).map { it.opptattAv }.map { it.displaytext }

    fun persist(opptattAv: FrivilligOpptattAv) = repository.persist(opptattAv)
    fun slett(id: Int) = repository.delete(
        "frivillig_id=?1",
        id
    )
}
