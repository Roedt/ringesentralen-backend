package no.roedt.ringesentralen.samtale.telefonsvarer

import jakarta.enterprise.context.Dependent
import no.roedt.person.PersonService
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.brukere.RingesentralenGroupID
import no.roedt.ringesentralen.ringer.Ringer
import no.roedt.ringesentralen.ringer.RingerService
import no.roedt.ringesentralen.samtale.PersistentSamtale
import no.roedt.ringesentralen.samtale.PersistentSamtaleRepository
import no.roedt.ringesentralen.samtale.Ringesesjon

@Dependent
class TelefonsvarerService(
    private val personService: PersonService,
    private val samtaleRepository: PersistentSamtaleRepository,
    private val ringerService: RingerService
) {
    fun postSvarFraTelefonsvarer(request: AutentisertTelefonsvarerRequest) {
        val optionalPerson = personService.finnFraTelefonnummer(request.request.telefonnummer)
        if (optionalPerson.isEmpty) {
            println("${request.request.telefonnummer} ringte og svarte ja til meir informasjon uten å være registrert i systemet")
            return
        }
        val person = optionalPerson.get()

        val systembruker = personService.systembruker()

        samtaleRepository.persist(
            PersistentSamtale(
                ringt = person.id.toInt(),
                ringer = ringerService.finnFraPerson(systembruker.id.toInt()).firstResult<Ringer>().id.toInt(),
                kommentar = "Resultat frå telefonsvar",
                ringesesjon = Ringesesjon.Valkamp2023.id,
                resultat = request.resultat().nr,
                modus = if (person.hypersysID != null) Modus.medlemmer else Modus.velgere
            )
        )

        if (RingesentralenGroupID.isBrukerEllerVenter(person.groupID())) return
        request.resultat().nesteGroupID?.nr?.let { personService.oppdaterRolle(it, person.id) }
    }
}
