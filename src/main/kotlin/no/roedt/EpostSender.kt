package no.roedt

import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import no.roedt.brukere.Epost
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class EpostSender(val mailer: Mailer) {

    @Inject
    @ConfigProperty(name = "quarkus.mailer.mock")
    lateinit var mock: String

    fun sendEpost(
        epost: Epost,
        mottaker: String?
    ) {
        if (mottaker == null || mottaker == "") {
            return
        }
        if (mock.toBoolean()) {
            println("Epostutsending er avskrudd. Ville elles sendt ${epost.tekstAaLoggeHvisDeaktivert}")
            return
        }
        println("Sender epost: ${epost.loggFoerSendingTekst}")
        mailer.send(
            Mail.withText(
                "$mottaker",
                epost.tittel,
                epost.tekst
            )
        )
    }
}
