package no.roedt.ringesentralen.sms

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class SMSService(
    private val smsRepository: SMSRepository,
    private val smsTilMottakerRepository: SMSTilMottakerRepository
) {

    fun registrerSMSForUtsending(request: AutentisertLagreSMSRequest): LagreSMSResponse {
        val innerRequest = request.request
        val sms = SMS(innerRequest.tekst)
        smsRepository.persist(sms)
        val smsId = sms.id
        innerRequest.mottakere
            .map {
                SMSTilMottaker(
                    sms_id = smsId,
                    mottaker_id = it,
                    utsendingsstatus = Utsendingsstatus.KlarTilUtsending
                )
            }
            .forEach {
                smsTilMottakerRepository.persist(it)
            }
        return LagreSMSResponse(id = smsId)
    }

    fun oppdaterUtsendingsstatus(request: AutentisertOppdaterSMSRequest) {
        val innerRequest = request.request
        innerRequest.mottakere.forEach {
            smsTilMottakerRepository.update("utsendingsstatus=?1 where mottaker_id=?2 and sms_id=?3", innerRequest.status, it, innerRequest.smsId)
        }
    }
}
