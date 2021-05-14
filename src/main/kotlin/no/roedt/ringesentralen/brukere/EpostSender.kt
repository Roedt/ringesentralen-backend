package no.roedt.ringesentralen.brukere

import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class EpostSender(var mailer: Mailer) {

    @Inject
    @ConfigProperty(name = "quarkus.mailer.mock")
    lateinit var mock: String

    fun sendEpost(person: Person, nyTilgang: GroupID) {
        if (person.email == null) {
            return
        }
        val eposttekst = "Hei, ${person.fornavn} ${person.etternavn}. " +
            "Du har no fått endra status i Raudts Ringesentral." + System.lineSeparator() +
            "Din nye status er: ${nyTilgang.skildring}" + System.lineSeparator().repeat(3) +
            "Dette er ein automatisk utsendt e-post."
        if (mock.toBoolean()) {
            println("Epostutsending er avskrudd. Ville elles sendt ${nyTilgang.name} til ${person.id}")
            return
        }
        println("Sender epost: Person med id ${person.id} har no fått ${nyTilgang.name}")
        mailer.send(
            Mail.withText(
                "${person.email}",
                "E-post frå Raudts Ringesentral",
                eposttekst
            )
        )
    }
}
