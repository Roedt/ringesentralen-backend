package no.roedt.ringesentralen.twilio

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import no.roedt.ringesentralen.sms.AutentisertSendSMSRequest
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class SMSSender(
    @ConfigProperty(name = "TWILIO_ACCOUNT_SID", defaultValue = "sid1")
    private val twilioAccountSid: String,

    @ConfigProperty(name = "TWILIO_AUTH_TOKEN", defaultValue = "token1")
    private val twilioAuthToken: String
) {

    @PostConstruct
    fun setUp() = Twilio.init(twilioAccountSid, twilioAuthToken)

    fun sendSMS(sendSMSRequest: AutentisertSendSMSRequest): Message = Message
        .creator(
            PhoneNumber(sendSMSRequest.request.til),
            PhoneNumber(sendSMSRequest.request.fra),
            sendSMSRequest.request.melding
        ).create()
}
