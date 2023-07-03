package no.roedt.ringesentralen.twilio

import com.twilio.rest.api.v2010.account.Message
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection(targets = [Message::class])
class TwilioRegisterForReflection
