package no.roedt.ringesentralen.twilio

import com.twilio.Twilio
import com.twilio.exception.ApiException
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import no.roedt.ringesentralen.sms.AutentisertSendSMSRequest
import no.roedt.token.SecretFactory

@ApplicationScoped
class SMSSender(
    private val secretFactory: SecretFactory
) {

    @PostConstruct
    fun setUp() = Twilio.init(secretFactory.getTwilioAccountSid(), secretFactory.getTwilioAuthToken())

    internal fun sendSMS(sendSMSRequest: AutentisertSendSMSRequest) {
        try {
            Message
                .creator(
                    PhoneNumber(sendSMSRequest.request.til),
                    PhoneNumber(sendSMSRequest.request.fra),
                    sendSMSRequest.request.melding
                ).create()
                .also { println("Sendte sms") }
        } catch (e: ApiException) {
            if (e.message?.contains("this appears to be a native image, in which case you may need to configure reflection for the class that is to be deserialized") == true) {
                e.printStackTrace()
            } else {
                throw e
            }
        }
    }
}
