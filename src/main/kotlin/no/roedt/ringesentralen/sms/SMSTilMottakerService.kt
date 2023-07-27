package no.roedt.ringesentralen.sms

import jakarta.enterprise.context.Dependent

@Dependent
class SMSTilMottakerService(internal val repository: SMSTilMottakerRepository) {
    fun slett(id: Int?) = repository.delete("mottaker_id=?1", id)
    fun persist(smsTilMottaker: SMSTilMottaker) = repository.persist(smsTilMottaker)
    fun oppdaterUtsendingsstatus(innerRequest: OppdaterSMSRequest, mottaker: Long) =
        repository.update(
            "utsendingsstatus=?1 where mottaker_id=?2 and sms_id=?3",
            innerRequest.status,
            mottaker,
            innerRequest.smsId
        )
}
