package no.roedt

import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import no.roedt.brukere.Epost
import no.roedt.person.Person
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class EpostSender(val mailer: Mailer) {

    @Inject
    @ConfigProperty(name = "quarkus.mailer.mock")
    lateinit var mock: String

    fun sendEpost(
        person: Person,
        epost: Epost
    ) {
        if (person.email == null || person.email == "") {
            return
        }
        if (mock.toBoolean()) {
            println("Epostutsending er avskrudd. Ville elles sendt ${epost.tekstAaLoggeHvisDeaktivert}")
            return
        }
        println("Sender epost: ${epost.loggFoerSendingTekst}")
        mailer.send(
            Mail.withText(
                "${person.email}",
                epost.tittel,
                epost.tekst
            )
        )
    }
}
