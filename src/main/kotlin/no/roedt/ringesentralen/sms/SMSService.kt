package no.roedt.ringesentralen.sms

import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class SMSService(
    private val smsRepository: SMSRepository,
    private val smsTilMottakerService: SMSTilMottakerService
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
                smsTilMottakerService.persist(it)
            }
        return LagreSMSResponse(id = smsId)
    }

    fun oppdaterUtsendingsstatus(request: AutentisertOppdaterSMSRequest) {
        val innerRequest = request.request
        innerRequest.mottakere.forEach { smsTilMottakerService.oppdaterUtsendingsstatus(innerRequest, it) }
    }
}
