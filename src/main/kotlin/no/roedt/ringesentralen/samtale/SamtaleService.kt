package no.roedt.ringesentralen.samtale

import jakarta.enterprise.context.Dependent
import no.roedt.ringesentralen.samtale.resultat.Resultat

@Dependent
class SamtaleService(internal val repository: PersistentSamtaleRepository) {
    fun flyttSamtalerMedDenneSomRingt(id: Int?, personId: Int) =
        repository.update("ringt=?1 where ringt=?2", id, personId)

    fun flyttSamtalerMedDenneSomRinger(id: Int?, id1: Int?) = repository.update("ringer=?1 where ringer=?2", id, id1)
    fun persist(persistentSamtale: PersistentSamtale) = repository.persist(persistentSamtale)
    fun samtalerUtenSvar(ringtID: Long): List<Int> =
        repository.list("ringt=?1 and resultat=${Resultat.Ikke_svar.nr}", ringtID.toInt())
            .map { it.resultat }.map { it }
}
