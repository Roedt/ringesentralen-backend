package no.roedt.lokallag

import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import no.roedt.hypersys.HypersysService
import org.eclipse.microprofile.faulttolerance.Fallback
import org.eclipse.microprofile.faulttolerance.Timeout

@ApplicationScoped
class Lokallagsynkroniserer(val hypersysService: HypersysService, val lokallagService: LokallagService) {
    @Timeout(value = 10000L)
    @Fallback(fallbackMethod = "hoppOverSynkronisering")
    fun onStart(
        @Observes event: StartupEvent
    ) {
        println("Henter alle lokallag fra Hypersys")
        val alleLokallagFraHypersys = hypersysService.getAlleLokallag()
        println("Henter alle lokallag fra systemet")
        val alleLokallagISystemet = lokallagService.listAll()
        val iHypersysMenIkkeIDatabasen =
            alleLokallagFraHypersys.filterNot { alleLokallagISystemet.map { i -> i.navn }.contains(it.name) }
        if (iHypersysMenIkkeIDatabasen.isNotEmpty()) {
            println("Lokallag som er i Hypersys, men ikkje i databasen:")
            println(iHypersysMenIkkeIDatabasen)
        }
        val forventaUtanFylke =
            setOf(
                "Faglig Utvalg",
                "Hele Norge",
                "Internasjonalt utvalg",
                "Kvinneutvalg",
                "Miljøpolitisk utvalg",
                "Rød Ungdom",
                "Rødt",
                "Udefinert",
                "Utland",
                "testlaget"
            )
        val iDatabasenUtanFylke =
            alleLokallagISystemet
                .filter { it.fylke == -1 }
                .map { it.navn }
                .filterNot { forventaUtanFylke.contains(it) }
        if (iDatabasenUtanFylke.isNotEmpty()) {
            println("Lokallag som er i databasen utan fylke:")
            println(iDatabasenUtanFylke)
        }
    }

    private fun hoppOverSynkronisering(event: StartupEvent) {
        println("Oppkopling mot Hypersys tok for lang tid, så hoppar over å sjekke om alle lokallag er med.")
    }
}
