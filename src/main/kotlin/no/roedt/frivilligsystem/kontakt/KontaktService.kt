package no.roedt.frivilligsystem.kontakt

import jakarta.enterprise.context.Dependent
import no.roedt.person.Person
import no.roedt.person.PersonService
import java.time.Instant

@Dependent
class KontaktService(internal val repository: KontaktRepository, internal val personService: PersonService) {
    fun flyttKontaktTilGeneriskTidligereMedlem(tidligereMedlem: Int?, personId: Int) =
        repository.update("registrert_av=?1 where registrert_av=?2", tidligereMedlem, personId)

    fun slett(id: Int) = repository.delete("frivillig_id=?1", id)
    fun hentKontakt(id: Int) = repository.list("frivillig_id", id).map { i ->
        KontaktResponse(
            frivillig_id = i.frivillig_id,
            tilbakemelding = i.tilbakemelding,
            registrert_av = personService.findById(i.registrert_av),
            datetime = i.datetime
        )
    }

    fun registrerKontakt(kontakt: AutentisertRegistrerKontaktRequest) = repository.persist(
        Kontakt(
            frivillig_id = kontakt.request.frivillig_id,
            tilbakemelding = kontakt.request.tilbakemelding,
            registrert_av = personService.finnFraHypersysId(kontakt.userId.userId)
                .firstResult<Person>().id.toInt(),
            datetime = Instant.now()
        )
    )
}
