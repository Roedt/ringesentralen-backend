package no.roedt.lokallag

import io.quarkus.runtime.StartupEvent
import no.roedt.hypersys.HypersysService
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
class Lokallagsynkroniserer(val hypersysService: HypersysService, val lokallagRepository: LokallagRepository) {
    fun onStart(@Observes event: StartupEvent) {
        val alleLokallagFraHypersys = hypersysService.getAlleLokallag()
        val alleLokallagISystemet = lokallagRepository.listAll()
        val iHypersysMenIkkeIDatabasen =
            alleLokallagFraHypersys.filterNot { alleLokallagISystemet.map { i -> i.navn }.contains(it.name) }
        println("Lokallag som er i Hypersys, men ikkje i databasen:")
        println(iHypersysMenIkkeIDatabasen)
    }
}
