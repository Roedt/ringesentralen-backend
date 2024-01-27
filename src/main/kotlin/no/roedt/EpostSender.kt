package no.roedt

import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import jakarta.enterprise.context.ApplicationScoped
import no.roedt.brukere.Epost
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class EpostSender(
    val mailer: Mailer,
    @ConfigProperty(name = "quarkus.mailer.mock")
    private val mock: Boolean
) {
    fun sendEpost(
        epost: Epost,
        mottaker: String?
    ) {
        if (mottaker == null || mottaker == "") {
            return
        }
        if (mock) {
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
        println("Sendte epost med tittel ${epost.tittel}")
    }
}
