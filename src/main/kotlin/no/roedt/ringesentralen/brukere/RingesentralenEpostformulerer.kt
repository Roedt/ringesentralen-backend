package no.roedt.ringesentralen.brukere

import no.roedt.EpostSender
import no.roedt.brukere.Epost
import no.roedt.person.GroupID
import no.roedt.person.Person
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RingesentralenEpostformulerer(val epostSender: EpostSender) {

    fun sendEpostOmEndraStatus(person: Person, nyTilgang: GroupID) = epostSender.sendEpost(
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

    fun sendEpostOmRegistrertSoMeFrivillig(person: Person) = epostSender.sendEpost(
        person,
        Epost(
            tekst = """Hei, ${person.fornavn} ${person.etternavn}. 
                Vi har nå registrert at du vil hjelpe Rødt å spre vårt politiske budskap i sosiale medier fram mot valget.
               ${System.lineSeparator()}
               Er dette feil? Kontakt oss på roedt@roedt.no, så fikser vi det.
               ${System.lineSeparator()}
               Dette er ein automatisk utsendt e-post.
            """.trimMargin(),
            loggFoerSendingTekst = "${person.fornavn} ${person.etternavn} er registrert som SoMe-frivillig",
            tekstAaLoggeHvisDeaktivert = "$person er registrert som SoMe-frivillig",
            tittel = "Du er nå registrert som sosiale media-aktivist"
        )
    )
}
