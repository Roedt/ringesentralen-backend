package no.roedt.ringesentralen.twilio

import com.twilio.converter.CurrencyDeserializer
import com.twilio.rest.api.v2010.account.Message
import com.twilio.rest.api.v2010.account.Message.AddressRetention
import com.twilio.rest.api.v2010.account.Message.UpdateStatus
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection(
    targets = [
        Message::class,
        CurrencyDeserializer::class,
        Message.Direction::class,
        Message.ContentRetention::class,
        AddressRetention::class,
        UpdateStatus::class,
        Message.Status::class,
        Message.ScheduleType::class
    ]
)
class TwilioRegisterForReflection
