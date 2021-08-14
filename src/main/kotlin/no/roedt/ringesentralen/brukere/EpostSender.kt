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

    fun sendEpostOmEndraStatus(person: Person, nyTilgang: GroupID) {
        sendEpost(
            person,
            Epost(
                tekst = "Hei, ${person.fornavn} ${person.etternavn}. " +
                        "Du har no fått endra status i Raudts Ringesentral." + System.lineSeparator() +
                        "Din nye status er: ${nyTilgang.skildring}" + System.lineSeparator().repeat(3) +
                        "Dette er ein automatisk utsendt e-post.",
                tekstAaLoggeHvisDeaktivert = "${nyTilgang.name} til ${person.id}",
                loggFoerSendingTekst = "Person med id ${person.id} har no fått ${nyTilgang.name}",
                tittel = "E-post frå Raudts Ringesentral"
            )
        )
    }

    private fun sendEpost(
        person: Person,
        epost: Epost
    ) {
        if (person.email == null) {
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
