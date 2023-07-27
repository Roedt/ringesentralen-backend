package no.roedt.frivilligsystem.registrer

import jakarta.enterprise.context.Dependent

@Dependent
class AktivitetForFrivilligService(internal val repository: AktivitetForFrivilligRepository) {
    fun slett(id: Int) = repository.delete("frivillig_id=?1", id)
    fun hent(id: Int?): List<AktivitetForFrivillig> = repository.list("frivillig_id", id)
    fun persist(aktivitet: AktivitetForFrivillig) = repository.persist(aktivitet)
}
